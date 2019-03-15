import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISongs, defaultValue } from 'app/shared/model/songs.model';

export const ACTION_TYPES = {
  FETCH_SONGS_LIST: 'songs/FETCH_SONGS_LIST',
  FETCH_SONGS: 'songs/FETCH_SONGS',
  CREATE_SONGS: 'songs/CREATE_SONGS',
  UPDATE_SONGS: 'songs/UPDATE_SONGS',
  DELETE_SONGS: 'songs/DELETE_SONGS',
  RESET: 'songs/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISongs>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SongsState = Readonly<typeof initialState>;

// Reducer

export default (state: SongsState = initialState, action): SongsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SONGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SONGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SONGS):
    case REQUEST(ACTION_TYPES.UPDATE_SONGS):
    case REQUEST(ACTION_TYPES.DELETE_SONGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SONGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SONGS):
    case FAILURE(ACTION_TYPES.CREATE_SONGS):
    case FAILURE(ACTION_TYPES.UPDATE_SONGS):
    case FAILURE(ACTION_TYPES.DELETE_SONGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SONGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SONGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SONGS):
    case SUCCESS(ACTION_TYPES.UPDATE_SONGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SONGS):
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

const apiUrl = 'api/songs';

// Actions

export const getEntities: ICrudGetAllAction<ISongs> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SONGS_LIST,
  payload: axios.get<ISongs>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISongs> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SONGS,
    payload: axios.get<ISongs>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISongs> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SONGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISongs> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SONGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISongs> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SONGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
