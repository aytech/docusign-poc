import React from "react";
import DocumentProps from "../models/DocumentProps";
import Document from "../models/Document";
import Attribute from "../models/Attribute";
import { Card, Col, ListGroup, Row } from "react-bootstrap";

export const Documents = ({documents}: DocumentProps) => {

  if (documents === null) {
    return null
  }

  return (
    documents.map((document: Document, index: number) => {
      return (
        <Row key={ index }>
          <Col>
            <Card className="mb-3">
              <Row>
                <Col lg="4" md="4" sm="4" xl="4" xs="4">
                  <Card.Img src={ document.resource.url }/>
                </Col>

                <Col lg="8" md="8" sm="8" xl="8" xs="8">
                  <Card.Body>
                    <Card.Title>
                      Document attributes:
                    </Card.Title>
                    <ListGroup className="mb-3">
                      {
                        document.attributes.map((attribute: Attribute, index: number) => {
                          return (
                            <ListGroup.Item key={ index }>
                              <strong>{ attribute.name }:</strong> { attribute.value }
                            </ListGroup.Item>
                          )
                        })
                      }
                      <ListGroup.Item>
                        <strong>File name:</strong> { document.fileName }
                      </ListGroup.Item>
                    </ListGroup>
                    <Card.Text>
                      <Card.Link href="#" onClick={ () => {
                        console.log("Signing")
                      } }>Sign</Card.Link>
                      <Card.Link href="#" onClick={ () => {
                        window.open(
                          "https://idm-ade-8.idm.awsdev.infor.com/ca/docusign.html?code=123&state=state",
                          "DocuSign Authorization",
                          "height=640,width=960,toolbar=no,menubar=no,scrollbars=no,location=no,status=no")
                        window.addEventListener("message", (event) => {
                          let code = ''
                          JSON.parse(event.data).data.replace(/\?/, '').split("&").every((param: string) => {
                            const parts = param.split("=")
                            if (parts[0] === 'code') {
                              code = parts[1]
                            }
                          })
                          console.info('Calling API with the code: ', code)
                        }, false)
                      } }>Login</Card.Link>
                    </Card.Text>
                  </Card.Body>
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
      )
    })
  )
}