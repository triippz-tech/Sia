import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IIgnored, defaultValue } from 'app/shared/model/ignored.model';

export const ACTION_TYPES = {
  FETCH_IGNORED_LIST: 'ignored/FETCH_IGNORED_LIST',
  FETCH_IGNORED: 'ignored/FETCH_IGNORED',
  CREATE_IGNORED: 'ignored/CREATE_IGNORED',
  UPDATE_IGNORED: 'ignored/UPDATE_IGNORED',
  DELETE_IGNORED: 'ignored/DELETE_IGNORED',
  RESET: 'ignored/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IIgnored>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type IgnoredState = Readonly<typeof initialState>;

// Reducer

export default (state: IgnoredState = initialState, action): IgnoredState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_IGNORED_LIST):
    case REQUEST(ACTION_TYPES.FETCH_IGNORED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_IGNORED):
    case REQUEST(ACTION_TYPES.UPDATE_IGNORED):
    case REQUEST(ACTION_TYPES.DELETE_IGNORED):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_IGNORED_LIST):
    case FAILURE(ACTION_TYPES.FETCH_IGNORED):
    case FAILURE(ACTION_TYPES.CREATE_IGNORED):
    case FAILURE(ACTION_TYPES.UPDATE_IGNORED):
    case FAILURE(ACTION_TYPES.DELETE_IGNORED):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_IGNORED_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_IGNORED):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_IGNORED):
    case SUCCESS(ACTION_TYPES.UPDATE_IGNORED):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_IGNORED):
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

const apiUrl = 'api/ignoreds';

// Actions

export const getEntities: ICrudGetAllAction<IIgnored> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_IGNORED_LIST,
  payload: axios.get<IIgnored>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IIgnored> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_IGNORED,
    payload: axios.get<IIgnored>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IIgnored> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_IGNORED,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IIgnored> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_IGNORED,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IIgnored> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_IGNORED,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
