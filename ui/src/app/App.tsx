import React, { useState } from "react"
import { Button, Card, Col, ListGroup, Modal, Row } from "react-bootstrap"
import { useDocumentQuery } from "../state/useDocumentQuery"
import "./App.css"
import { Documents } from "../documents/Documents";
import Document from "../models/Document";

export const App = () => {
  const {
    data: documentsData,
    error: documentsError,
    loading: documentsLoading
  } = useDocumentQuery<Document[]>()
  const [ show, setShow ] = useState(false)

  const documentsLoadingElement = documentsLoading === true || documentsData === null ? <span>Loading...</span> : null
  const documentElements = documentsData === null ? null : (
    documentsData.map((document: Document, index: number) => {
      return (
        <Row key={ index }>
          <Documents document={ document }/>
        </Row>
      )
    })
  )

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
        <Col>
          <h2>Documents</h2>
          { documentsLoadingElement }
        </Col>
      </Row>
      { documentElements }
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