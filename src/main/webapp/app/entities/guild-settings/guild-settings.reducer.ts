import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGuildSettings, defaultValue } from 'app/shared/model/guild-settings.model';

export const ACTION_TYPES = {
  FETCH_GUILDSETTINGS_LIST: 'guildSettings/FETCH_GUILDSETTINGS_LIST',
  FETCH_GUILDSETTINGS: 'guildSettings/FETCH_GUILDSETTINGS',
  CREATE_GUILDSETTINGS: 'guildSettings/CREATE_GUILDSETTINGS',
  UPDATE_GUILDSETTINGS: 'guildSettings/UPDATE_GUILDSETTINGS',
  DELETE_GUILDSETTINGS: 'guildSettings/DELETE_GUILDSETTINGS',
  RESET: 'guildSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildSettingsState = initialState, action): GuildSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_GUILDSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_GUILDSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_GUILDSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDSETTINGS):
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

const apiUrl = 'api/guild-settings';

// Actions

export const getEntities: ICrudGetAllAction<IGuildSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDSETTINGS_LIST,
  payload: axios.get<IGuildSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSETTINGS,
    payload: axios.get<IGuildSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
