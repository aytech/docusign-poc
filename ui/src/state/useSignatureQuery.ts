import { useReducer } from "react";
import Document from "../models/Document";

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

export const useSignatureQuery = <TData>(
  document: Document
) => {
  const fetchReducer = reducer<TData>()
  const [ state, dispatch ] = useReducer(fetchReducer, {
    data: null,
    error: false,
    loading: false
  })

  const fetchRequest = () => {
    const fetchApi = async () => {
      const response = await fetch("/sign", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          subject: "Sample signature request - POC",
          message: "DocuSign service for Infor.",
          pids: [ document.pid ],
          recipients: [
            {
              name: "Oleg Yapparov",
              email: "Oleg.Yapparov@infor.com"
            }
          ],
          templates: []
        })
      })
      return response.json()
    }
    fetchApi().then(response => {
      console.log('Response: ', response)
    })
  }
  return { ...state, fetchRequest }
}