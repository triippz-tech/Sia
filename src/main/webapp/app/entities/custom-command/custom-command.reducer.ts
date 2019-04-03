import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomCommand, defaultValue } from 'app/shared/model/custom-command.model';

export const ACTION_TYPES = {
  FETCH_CUSTOMCOMMAND_LIST: 'customCommand/FETCH_CUSTOMCOMMAND_LIST',
  FETCH_CUSTOMCOMMAND: 'customCommand/FETCH_CUSTOMCOMMAND',
  CREATE_CUSTOMCOMMAND: 'customCommand/CREATE_CUSTOMCOMMAND',
  UPDATE_CUSTOMCOMMAND: 'customCommand/UPDATE_CUSTOMCOMMAND',
  DELETE_CUSTOMCOMMAND: 'customCommand/DELETE_CUSTOMCOMMAND',
  SET_BLOB: 'customCommand/SET_BLOB',
  RESET: 'customCommand/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomCommand>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CustomCommandState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomCommandState = initialState, action): CustomCommandState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMCOMMAND_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMCOMMAND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMCOMMAND):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMCOMMAND):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMCOMMAND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMCOMMAND_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMCOMMAND):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMCOMMAND):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMCOMMAND):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMCOMMAND):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMCOMMAND_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMCOMMAND):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMCOMMAND):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMCOMMAND):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMCOMMAND):
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

const apiUrl = 'api/custom-commands';

// Actions

export const getEntities: ICrudGetAllAction<ICustomCommand> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMCOMMAND_LIST,
    payload: axios.get<ICustomCommand>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICustomCommand> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMCOMMAND,
    payload: axios.get<ICustomCommand>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomCommand> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMCOMMAND,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomCommand> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMCOMMAND,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomCommand> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMCOMMAND,
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
