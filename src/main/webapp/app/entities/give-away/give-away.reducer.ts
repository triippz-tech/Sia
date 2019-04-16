import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGiveAway, defaultValue } from 'app/shared/model/give-away.model';

export const ACTION_TYPES = {
  FETCH_GIVEAWAY_LIST: 'giveAway/FETCH_GIVEAWAY_LIST',
  FETCH_GIVEAWAY: 'giveAway/FETCH_GIVEAWAY',
  CREATE_GIVEAWAY: 'giveAway/CREATE_GIVEAWAY',
  UPDATE_GIVEAWAY: 'giveAway/UPDATE_GIVEAWAY',
  DELETE_GIVEAWAY: 'giveAway/DELETE_GIVEAWAY',
  SET_BLOB: 'giveAway/SET_BLOB',
  RESET: 'giveAway/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGiveAway>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type GiveAwayState = Readonly<typeof initialState>;

// Reducer

export default (state: GiveAwayState = initialState, action): GiveAwayState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GIVEAWAY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GIVEAWAY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GIVEAWAY):
    case REQUEST(ACTION_TYPES.UPDATE_GIVEAWAY):
    case REQUEST(ACTION_TYPES.DELETE_GIVEAWAY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GIVEAWAY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GIVEAWAY):
    case FAILURE(ACTION_TYPES.CREATE_GIVEAWAY):
    case FAILURE(ACTION_TYPES.UPDATE_GIVEAWAY):
    case FAILURE(ACTION_TYPES.DELETE_GIVEAWAY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GIVEAWAY_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GIVEAWAY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GIVEAWAY):
    case SUCCESS(ACTION_TYPES.UPDATE_GIVEAWAY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GIVEAWAY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/give-aways';

// Actions

export const getEntities: ICrudGetAllAction<IGiveAway> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GIVEAWAY_LIST,
    payload: axios.get<IGiveAway>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IGiveAway> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GIVEAWAY,
    payload: axios.get<IGiveAway>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGiveAway> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GIVEAWAY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGiveAway> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GIVEAWAY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGiveAway> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GIVEAWAY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
