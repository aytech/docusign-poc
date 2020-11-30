import Document from "./Document";
import Template from "./Template";

export default interface DocumentsResponse {
  documents: Document[]
  templates: Template[]
}