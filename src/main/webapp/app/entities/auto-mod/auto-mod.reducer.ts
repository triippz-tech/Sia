import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAutoMod, defaultValue } from 'app/shared/model/auto-mod.model';

export const ACTION_TYPES = {
  FETCH_AUTOMOD_LIST: 'autoMod/FETCH_AUTOMOD_LIST',
  FETCH_AUTOMOD: 'autoMod/FETCH_AUTOMOD',
  CREATE_AUTOMOD: 'autoMod/CREATE_AUTOMOD',
  UPDATE_AUTOMOD: 'autoMod/UPDATE_AUTOMOD',
  DELETE_AUTOMOD: 'autoMod/DELETE_AUTOMOD',
  RESET: 'autoMod/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutoMod>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AutoModState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoModState = initialState, action): AutoModState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOMOD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOMOD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOMOD):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOMOD):
    case REQUEST(ACTION_TYPES.DELETE_AUTOMOD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOMOD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOMOD):
    case FAILURE(ACTION_TYPES.CREATE_AUTOMOD):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOMOD):
    case FAILURE(ACTION_TYPES.DELETE_AUTOMOD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMOD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMOD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOMOD):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOMOD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOMOD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/auto-mods';

// Actions

export const getEntities: ICrudGetAllAction<IAutoMod> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUTOMOD_LIST,
  payload: axios.get<IAutoMod>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAutoMod> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOMOD,
    payload: axios.get<IAutoMod>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutoMod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOMOD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAutoMod> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOMOD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutoMod> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOMOD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
