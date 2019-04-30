import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDiscordGuild, defaultValue } from 'app/shared/model/discord-guild.model';

export const ACTION_TYPES = {
  FETCH_DISCORDGUILD_LIST: 'discordGuild/FETCH_DISCORDGUILD_LIST',
  FETCH_DISCORDGUILD: 'discordGuild/FETCH_DISCORDGUILD',
  CREATE_DISCORDGUILD: 'discordGuild/CREATE_DISCORDGUILD',
  UPDATE_DISCORDGUILD: 'discordGuild/UPDATE_DISCORDGUILD',
  DELETE_DISCORDGUILD: 'discordGuild/DELETE_DISCORDGUILD',
  RESET: 'discordGuild/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDiscordGuild>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DiscordGuildState = Readonly<typeof initialState>;

// Reducer

export default (state: DiscordGuildState = initialState, action): DiscordGuildState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DISCORDGUILD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DISCORDGUILD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DISCORDGUILD):
    case REQUEST(ACTION_TYPES.UPDATE_DISCORDGUILD):
    case REQUEST(ACTION_TYPES.DELETE_DISCORDGUILD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DISCORDGUILD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DISCORDGUILD):
    case FAILURE(ACTION_TYPES.CREATE_DISCORDGUILD):
    case FAILURE(ACTION_TYPES.UPDATE_DISCORDGUILD):
    case FAILURE(ACTION_TYPES.DELETE_DISCORDGUILD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCORDGUILD_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCORDGUILD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DISCORDGUILD):
    case SUCCESS(ACTION_TYPES.UPDATE_DISCORDGUILD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DISCORDGUILD):
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

const apiUrl = 'api/discord-guilds';

// Actions

export const getEntities: ICrudGetAllAction<IDiscordGuild> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DISCORDGUILD_LIST,
    payload: axios.get<IDiscordGuild>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDiscordGuild> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DISCORDGUILD,
    payload: axios.get<IDiscordGuild>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDiscordGuild> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DISCORDGUILD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDiscordGuild> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DISCORDGUILD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDiscordGuild> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DISCORDGUILD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
