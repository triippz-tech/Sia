import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPoll, defaultValue } from 'app/shared/model/poll.model';

export const ACTION_TYPES = {
  FETCH_POLL_LIST: 'poll/FETCH_POLL_LIST',
  FETCH_POLL: 'poll/FETCH_POLL',
  CREATE_POLL: 'poll/CREATE_POLL',
  UPDATE_POLL: 'poll/UPDATE_POLL',
  DELETE_POLL: 'poll/DELETE_POLL',
  RESET: 'poll/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPoll>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PollState = Readonly<typeof initialState>;

// Reducer

export default (state: PollState = initialState, action): PollState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POLL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POLL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POLL):
    case REQUEST(ACTION_TYPES.UPDATE_POLL):
    case REQUEST(ACTION_TYPES.DELETE_POLL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POLL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POLL):
    case FAILURE(ACTION_TYPES.CREATE_POLL):
    case FAILURE(ACTION_TYPES.UPDATE_POLL):
    case FAILURE(ACTION_TYPES.DELETE_POLL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POLL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_POLL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POLL):
    case SUCCESS(ACTION_TYPES.UPDATE_POLL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POLL):
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

const apiUrl = 'api/polls';

// Actions

export const getEntities: ICrudGetAllAction<IPoll> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_POLL_LIST,
  payload: axios.get<IPoll>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPoll> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POLL,
    payload: axios.get<IPoll>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPoll> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POLL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPoll> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POLL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPoll> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POLL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
