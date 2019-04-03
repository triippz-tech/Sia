import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGuildRoles, defaultValue } from 'app/shared/model/guild-roles.model';

export const ACTION_TYPES = {
  FETCH_GUILDROLES_LIST: 'guildRoles/FETCH_GUILDROLES_LIST',
  FETCH_GUILDROLES: 'guildRoles/FETCH_GUILDROLES',
  CREATE_GUILDROLES: 'guildRoles/CREATE_GUILDROLES',
  UPDATE_GUILDROLES: 'guildRoles/UPDATE_GUILDROLES',
  DELETE_GUILDROLES: 'guildRoles/DELETE_GUILDROLES',
  RESET: 'guildRoles/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildRoles>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildRolesState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildRolesState = initialState, action): GuildRolesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDROLES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDROLES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDROLES):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDROLES):
    case REQUEST(ACTION_TYPES.DELETE_GUILDROLES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDROLES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDROLES):
    case FAILURE(ACTION_TYPES.CREATE_GUILDROLES):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDROLES):
    case FAILURE(ACTION_TYPES.DELETE_GUILDROLES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDROLES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDROLES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDROLES):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDROLES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDROLES):
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

const apiUrl = 'api/guild-roles';

// Actions

export const getEntities: ICrudGetAllAction<IGuildRoles> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDROLES_LIST,
  payload: axios.get<IGuildRoles>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildRoles> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDROLES,
    payload: axios.get<IGuildRoles>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildRoles> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDROLES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildRoles> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDROLES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildRoles> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDROLES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
