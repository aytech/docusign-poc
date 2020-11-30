import Resource from "./Resource";
import Attribute from "./Attribute";
import Envelope from "./Envelope";

export default interface Document {
  pid: string
  author: string
  fileName: string
  acl: string
  resource: Resource
  attributes: Attribute[],
  envelopes: Envelope[]
}