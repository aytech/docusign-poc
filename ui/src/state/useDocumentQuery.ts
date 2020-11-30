import { useCallback, useEffect, useReducer } from "react";

interface State<TData> {
  data: TData | null
  loading: boolean
  error: boolean
}

type Action<TData> =
  | { type: 'FETCH' }
  | { type: 'FETCH_SUCCESS', data: TData }
  | { type: 'FETCH_ERROR' }

const reducer = <TData>() => (
  state: State<TData>,
  action: Action<TData>
): State<TData> => {
  switch (action.type) {
    case "FETCH":
      return { ...state, loading: true }
    case "FETCH_ERROR":
      return { ...state, error: true, loading: false }
    case "FETCH_SUCCESS":
      return { data: action.data, error: false, loading: false }
    default:
      throw new Error("Cannot fetch data")
  }
}

export const useDocumentQuery = <TData>() => {
  const fetchReducer = reducer<TData>()
  const [ state, dispatch ] = useReducer(fetchReducer, {
    data: null,
    error: false,
    loading: true
  })
  const fetchCallback = useCallback(async () => {
    const fetchApi = async () => {
      const response = await fetch("/documents", {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      })
      return response.json()
    }
    fetchApi()
      .then((response: TData) => {
        dispatch({ type: "FETCH_SUCCESS", data: response })
      })
      .catch(_error => {
        dispatch({ type: "FETCH_ERROR" })
      })
  }, [])

  useEffect(() => {
    fetchCallback().then(_response => {
      dispatch({ type: "FETCH" })
    })
  }, [])

  return { ...state, fetchCallback }
}