import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGuildMusicSettings, defaultValue } from 'app/shared/model/guild-music-settings.model';

export const ACTION_TYPES = {
  FETCH_GUILDMUSICSETTINGS_LIST: 'guildMusicSettings/FETCH_GUILDMUSICSETTINGS_LIST',
  FETCH_GUILDMUSICSETTINGS: 'guildMusicSettings/FETCH_GUILDMUSICSETTINGS',
  CREATE_GUILDMUSICSETTINGS: 'guildMusicSettings/CREATE_GUILDMUSICSETTINGS',
  UPDATE_GUILDMUSICSETTINGS: 'guildMusicSettings/UPDATE_GUILDMUSICSETTINGS',
  DELETE_GUILDMUSICSETTINGS: 'guildMusicSettings/DELETE_GUILDMUSICSETTINGS',
  RESET: 'guildMusicSettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildMusicSettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildMusicSettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildMusicSettingsState = initialState, action): GuildMusicSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDMUSICSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDMUSICSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDMUSICSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDMUSICSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_GUILDMUSICSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDMUSICSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDMUSICSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_GUILDMUSICSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDMUSICSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_GUILDMUSICSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDMUSICSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDMUSICSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDMUSICSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDMUSICSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDMUSICSETTINGS):
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

const apiUrl = 'api/guild-music-settings';

// Actions

export const getEntities: ICrudGetAllAction<IGuildMusicSettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDMUSICSETTINGS_LIST,
  payload: axios.get<IGuildMusicSettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildMusicSettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDMUSICSETTINGS,
    payload: axios.get<IGuildMusicSettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildMusicSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDMUSICSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildMusicSettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDMUSICSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildMusicSettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDMUSICSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
