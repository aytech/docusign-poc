import Template from "./Template";
import Document from "./Document";

export default interface SignatureRequest {
  document: Document
  template: Template
}