import React from "react";
import DocumentProps from "../models/DocumentProps";
import Attribute from "../models/Attribute";
import {
  Card,
  Col,
  ListGroup,
  Row
} from "react-bootstrap";
import { useSignatureQuery } from "../state/useSignatureQuery";
import Document from "../models/Document";
import Envelope from "../models/Envelope";

export const Documents = ({ document }: DocumentProps) => {

  const {
    fetchRequest,
    data: signData,
    error: signError,
    loading: signLoading,
  } = useSignatureQuery<Document>(document)

  const authorize = async (code: string) => {
    const response = await fetch("/authenticate", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ code })
    })
    return response.json()
  }

  return (
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
              <Card.Title>
                Document envelopes:
              </Card.Title>
              <ListGroup className="mb-3">
                {
                  document.envelopes.map((envelope: Envelope, index: number) => {
                    return (
                      <ListGroup.Item key={ index }>
                        <strong>ID:</strong> { envelope.signature },&nbsp;
                        <strong>Status:</strong> { envelope.status },&nbsp;
                        <strong>Version:</strong> { envelope.version }
                      </ListGroup.Item>
                    )
                  })
                }
              </ListGroup>
              <Card.Text>
                <Card.Link href="#" onClick={ () => {
                  fetchRequest()
                } }>Sign</Card.Link>
                <Card.Link href="#" onClick={ () => {
                  window.open(
                    "https://account-d.docusign.com/oauth/auth?response_type=code&scope=extended%20impersonation%20signature&client_id=0a12a171-7715-4ea0-b29f-a32236c1fa11&redirect_uri=https%3A%2F%2Fidm-ade-5.idm.awsdev.infor.com%2Fca%2Fdocusign.html",
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
                    authorize(code).then(response => {
                      console.log('Response: ', response)
                    })
                  }, false)
                } }>Login</Card.Link>
              </Card.Text>
            </Card.Body>
          </Col>
        </Row>
      </Card>
    </Col>
  )
}