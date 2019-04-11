import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGuildEvent, defaultValue } from 'app/shared/model/guild-event.model';

export const ACTION_TYPES = {
  FETCH_GUILDEVENT_LIST: 'guildEvent/FETCH_GUILDEVENT_LIST',
  FETCH_GUILDEVENT: 'guildEvent/FETCH_GUILDEVENT',
  CREATE_GUILDEVENT: 'guildEvent/CREATE_GUILDEVENT',
  UPDATE_GUILDEVENT: 'guildEvent/UPDATE_GUILDEVENT',
  DELETE_GUILDEVENT: 'guildEvent/DELETE_GUILDEVENT',
  SET_BLOB: 'guildEvent/SET_BLOB',
  RESET: 'guildEvent/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildEvent>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type GuildEventState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildEventState = initialState, action): GuildEventState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDEVENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDEVENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDEVENT):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDEVENT):
    case REQUEST(ACTION_TYPES.DELETE_GUILDEVENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDEVENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDEVENT):
    case FAILURE(ACTION_TYPES.CREATE_GUILDEVENT):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDEVENT):
    case FAILURE(ACTION_TYPES.DELETE_GUILDEVENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDEVENT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDEVENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDEVENT):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDEVENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDEVENT):
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

const apiUrl = 'api/guild-events';

// Actions

export const getEntities: ICrudGetAllAction<IGuildEvent> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDEVENT_LIST,
    payload: axios.get<IGuildEvent>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IGuildEvent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDEVENT,
    payload: axios.get<IGuildEvent>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDEVENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildEvent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDEVENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildEvent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDEVENT,
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
