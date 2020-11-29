import React, { useState } from "react"
import { Button, Card, Col, ListGroup, Modal, Row } from "react-bootstrap"
import { useDocumentQuery } from "../state/useDocumentQuery"
import "./App.css"
import { useSignatureQuery } from "../state/useSignatureQuery";
import { Documents } from "../documents/Documents";

export const App = () => {
  const {
    data: documentsData,
    error: documentsError,
    loading: documentsLoading
  } = useDocumentQuery<Document[]>()
  const {
    fetchCallback,
    data: signData,
    error: signError,
    loading: signLoading,
  } = useSignatureQuery<{}>("pids here")
  const [ show, setShow ] = useState(false)

  const documentsLoadingElement = documentsLoading === true || documentsData === null ? <span>Loading...</span> : null
  // const documentsList = documentsData === null ? null : (
  //   documentsData.map((document: Document, index: number) => {
  //     return (
  //       <Row key={ index }>
  //         <Col>
  //           <Card className="mb-3">
  //             <Row>
  //               <Col lg="4" md="4" sm="4" xl="4" xs="4">
  //                 <Card.Img src={ document.resource.url }/>
  //               </Col>
  //
  //               <Col lg="8" md="8" sm="8" xl="8" xs="8">
  //                 <Card.Body>
  //                   <Card.Title>
  //                     Document attributes:
  //                   </Card.Title>
  //                   <ListGroup className="mb-3">
  //                     {
  //                       document.attributes.map((attribute: Attribute, index: number) => {
  //                         return (
  //                           <ListGroup.Item key={ index }>
  //                             <strong>{ attribute.name }:</strong> { attribute.value }
  //                           </ListGroup.Item>
  //                         )
  //                       })
  //                     }
  //                     <ListGroup.Item>
  //                       <strong>File name:</strong> { document.fileName }
  //                     </ListGroup.Item>
  //                   </ListGroup>
  //                   <Card.Text>
  //                     <Card.Link href="#" onClick={ () => {
  //                       console.log("Signing")
  //                     } }>Sign</Card.Link>
  //                     <Card.Link href="#" onClick={ () => {
  //                       window.open(
  //                         "https://idm-ade-8.idm.awsdev.infor.com/ca/docusign.html?code=123&state=state",
  //                         "DocuSign Authorization",
  //                         "height=640,width=960,toolbar=no,menubar=no,scrollbars=no,location=no,status=no")
  //                       window.addEventListener("message", (event) => {
  //                         let code = ''
  //                         JSON.parse(event.data).data.replace(/\?/, '').split("&").every((param: string) => {
  //                           const parts = param.split("=")
  //                           if (parts[0] === 'code') {
  //                             code = parts[1]
  //                           }
  //                         })
  //                         console.info('Calling API with the code: ', code)
  //                       }, false)
  //                     } }>Login</Card.Link>
  //                   </Card.Text>
  //                 </Card.Body>
  //               </Col>
  //             </Row>
  //           </Card>
  //         </Col>
  //       </Row>
  //     )
  //   })
  // )

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
      <Documents documents={ documentsData }/>
      <Row>
        <Col>
          <h2>Envelopes</h2>
        </Col>
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