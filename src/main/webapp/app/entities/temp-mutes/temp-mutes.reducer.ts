import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITempMutes, defaultValue } from 'app/shared/model/temp-mutes.model';

export const ACTION_TYPES = {
  FETCH_TEMPMUTES_LIST: 'tempMutes/FETCH_TEMPMUTES_LIST',
  FETCH_TEMPMUTES: 'tempMutes/FETCH_TEMPMUTES',
  CREATE_TEMPMUTES: 'tempMutes/CREATE_TEMPMUTES',
  UPDATE_TEMPMUTES: 'tempMutes/UPDATE_TEMPMUTES',
  DELETE_TEMPMUTES: 'tempMutes/DELETE_TEMPMUTES',
  RESET: 'tempMutes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITempMutes>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TempMutesState = Readonly<typeof initialState>;

// Reducer

export default (state: TempMutesState = initialState, action): TempMutesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEMPMUTES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEMPMUTES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEMPMUTES):
    case REQUEST(ACTION_TYPES.UPDATE_TEMPMUTES):
    case REQUEST(ACTION_TYPES.DELETE_TEMPMUTES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEMPMUTES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEMPMUTES):
    case FAILURE(ACTION_TYPES.CREATE_TEMPMUTES):
    case FAILURE(ACTION_TYPES.UPDATE_TEMPMUTES):
    case FAILURE(ACTION_TYPES.DELETE_TEMPMUTES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPMUTES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPMUTES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEMPMUTES):
    case SUCCESS(ACTION_TYPES.UPDATE_TEMPMUTES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEMPMUTES):
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

const apiUrl = 'api/temp-mutes';

// Actions

export const getEntities: ICrudGetAllAction<ITempMutes> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TEMPMUTES_LIST,
  payload: axios.get<ITempMutes>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITempMutes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEMPMUTES,
    payload: axios.get<ITempMutes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITempMutes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEMPMUTES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITempMutes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEMPMUTES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITempMutes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEMPMUTES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
