import React, { useState } from "react"
import { Button, Card, Col, Modal, Row } from "react-bootstrap"
import { useDocumentQuery } from "../state/useDocumentQuery"
import "./App.css"
import { useSignatureQuery } from "../state/useSignatureQuery";

interface Resource {
  name: string
  size: number
  mime: string
  fileName: string
  url: string
}

interface Attribute {
  name: string
  type: string
  value?: string
}

interface Document {
  pid: string
  author: string
  fileName: string
  acl: string
  resource: Resource
  attributes: Attribute[]
}

export const App = () => {
  const {data, error, loading} = useDocumentQuery<Document[]>()
  const {
    fetchCallback,
    data: signData,
    error: signError,
    loading: signLoading,
  } = useSignatureQuery<{}>("pids here")
  const [ show, setShow ] = useState(false)

  if (loading === true || data === null) {
    return <h2>Loading...</h2>
  }

  return (
    <React.Fragment>
      <div className="py-5 text-center">
        <img className="d-block mx-auto mb-4" src="/Infor-128.png" alt="Infor logo"/>
        <h2>DocuSign integration POC</h2>
        <p className="lead">
          Demo app to showcase integration points with IDM
        </p>
      </div>
      <Row>
        {
          data.map((document: Document, index: number) => {
            return (
              <Col key={ index }>
                <Card>
                  <Card.Img variant="top" src={ document.resource.url }/>
                  <Card.Body>
                    <Card.Title>{ document.pid }</Card.Title>
                    <Card.Text>{ document.fileName }</Card.Text>
                    <Button variant="primary" onClick={ () => {
                      fetchCallback()
                    } }>Signature</Button>
                  </Card.Body>
                </Card>
              </Col>
            )
          })
        }
      </Row>
      <Modal show={ show } onHide={ () => {
        setShow(false)
      } }>
        <Modal.Header closeButton>
          <Modal.Title>Modal title</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>Modal body text goes here.</p>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary">Close</Button>
          <Button variant="primary">Save changes</Button>
        </Modal.Footer>
      </Modal>
    </React.Fragment>
  )
}