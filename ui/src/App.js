import React from 'react'
import './App.css';

const FAKING_APIS = false;

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      documents: [],
      selectedPids: [],
      templates: null,
      activeTab: null
    }

    this.getDocuments = this.getDocuments.bind(this);
    this.signatureButtonClicked = this.signatureButtonClicked.bind(this);
    this.setTab = this.setTab.bind(this);
    this.sendEnvelope = this.sendEnvelope.bind(this);
  }

  componentDidMount() {
    window.addEventListener('message', this.handleWindowMessage);
    window.addEventListener('storage', this.handleStorage);
  }

  componentWillUnmount() {
    window.removeEventListener('message', this.handleWindowMessage);
    window.removeEventListener('storage', this.handleStorage);
  }

  handleWindowMessage(event) {
    console.log(event);
    let response;
    try {
      response = JSON.parse(event.data);
    } catch (e) {
      console.warn('Could not parse message: ', event);
      return;
    }

    if (response.channel !== 'idmclient') {
      console.warn('Wrong message channel: ', response.channel);
      return;
    }

    const code = new URLSearchParams(response.data).get('code');
    const options = { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ code }) };
    const url = '/authenticate';
    fetch(url, options)
      .then(response => response.json())
      .then(response => {
        console.log(response);
        this.signatureButtonClicked();
      });
  }

  handleStorage(event) {
    console.log(event);
    if (event.key !== 'idm_docusign_popup_return_value') {
      return null;
    }

    const value = new URLSearchParams(event.newValue).get('code');
    if (value === null || value.length === 0) {
      return null;
    }

    localStorage.removeItem(event.key);
    const code = new URLSearchParams(value).get('code');
    const options = { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ code }) };
    const url = '/authenticate';
    fetch(url, options)
      .then(response => response.json())
      .then(response => {
        console.log(response);
        this.signatureButtonClicked();
      });
  }

  getDocuments() {
    if (FAKING_APIS) {
      const documents = require('./data/GET_Documents');
      this.setState({ documents, selectedPids: [] });
      return;
    }

    const url = '/documents';
    fetch(url)
      .then(response => response.json())
      .then(documents => this.setState({ documents, selectedPids: [] }));
  }

  setSelectedItem(pid) {
    if (this.state.activeTab === 'signatureForm') {
      return;
    }

    const selectedPids = this.state.selectedPids.slice();
    const index = selectedPids.indexOf(pid)
    if (index === -1) {
      selectedPids.push(pid);
    } else {
      selectedPids.splice(index, 1);
    }
    this.setState({ selectedPids });
  }

  signatureButtonClicked() {
    const onTemplatesLoaded = (response) => {
      if (response.authorized) {
        this.setState({ templates: response.templates, activeTab: 'signatureForm' });
      } else {
        popupWindow(response.loginUrl, 'Docusign login', window, 500, 500);
      }
    }

    if (Array.isArray(this.state.templates)) {
      this.setState({ activeTab: 'signatureForm' });
    } else {
      if (FAKING_APIS) {
        const response = require('./data/GET_Templates');
        onTemplatesLoaded(response);
        return;
      }

      const url = '/templates';
      fetch(url)
        .then(response => response.json())
        .then(response => onTemplatesLoaded(response));
    }
  }

  sendEnvelope(templateName) {
    if (templateName === null) {
      return;
    }

    const templateData = this.state.templates.find(template => template.name === templateName);

    const data = {
      subject: templateData.subject ?? 'Subject',
      message: templateData.message ?? 'Message',
      pids: this.state.selectedPids,
      recipients: [
        {
          name: "Oleg Yapparov",
          email: "Oleg.Yapparov@infor.com"
        }
      ],
      templates: [templateData]
    }
    const options = { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) };
    const url = '/sign';
    fetch(url, options)
      .then(response => response.json())
      .then(result => console.log(result));

    this.setState({ activeTab: null });
  }

  setTab(activeTab) {
    this.setState({ activeTab });
  }

  showEnvelopes(e, item) {
    e.stopPropagation();
    if (this.state.activeTab === 'signatureForm') {
      return;
    }

    this.showEnvelopesForPid = item;
    this.setState({ activeTab: 'envelopes' });
  }

  render() {
    const selectedItems = this.state.documents.filter(
      document => this.state.selectedPids.indexOf(document.pid) > -1
    )
    const itemForEnvelopes = this.state.documents.find(
      document => document.pid === this.showEnvelopesForPid
    );

    let secondSection = <SignatureButtonTab items={selectedItems} signatureButtonClicked={this.signatureButtonClicked} />
    switch (this.state.activeTab) {
      case 'envelopes':
        secondSection = <EnvelopesTab item={itemForEnvelopes} setTab={this.setTab} />
        break;
      case 'signatureForm':
        secondSection = <SignatureFormTab templates={this.state.templates} sendEnvelope={this.sendEnvelope} setTab={this.setTab} />
    }

    return (
      <div className="App">
        <div>
          <section>
            <button type="button" onClick={this.getDocuments}>Get documents</button>
            <SearchResults
              documents={this.state.documents}
              onItemClicked={pid => this.setSelectedItem(pid)}
              selectedPids={this.state.selectedPids}
              showEnvelopesClicked={(event, pid) => this.showEnvelopes(event, pid)}
            />
          </section>
          <section className="secondSection">
            {secondSection}
          </section>
        </div>
      </div>
    );
  }
}

function SearchResults(props) {
  const documents = props.documents.map(
    item => {
      const imageSrc = item.resource.url ?? process.env.PUBLIC_URL + '/default.png';
      const alt = item.fileName ?? 'default image';
      const displayName = getAttributeByName(item, 'Name').value;

      const imageOrButton = item.envelopes.length > 0
        ? <button onClick={(e) => props.showEnvelopesClicked(e, item.pid)}>Show envelopes</button>
        : <img src={imageSrc} alt={alt}></img>

      return <li
        key={item.pid}
        onClick={() => props.onItemClicked(item.pid)}
        className={props.selectedPids.indexOf(item.pid) > -1 ? 'selected' : ''}
      >
        <div>
          {imageOrButton}
        </div>
        <div>
          <p className="attribute"><span>Name:</span><span>{displayName}</span></p>
          <p className="attribute"><span>Author:</span><span>{item.author}</span></p>
        </div>
      </li>
    }
  )
  const searchResult = documents.length
    ? <ul>{documents}</ul>
    : <p className="noResults">No search results</p>

  return <div className="SearchResults">
    {searchResult}
  </div>
}

function SignatureButtonTab(props) {
  const items = props.items;
  const documentWord = items.length > 1 ? "Some document" : "Document";

  let excuses = [];
  if (items.some(item => typeof item.fileName !== 'string')) {
    excuses.push(<li key="file">{documentWord} has no file attached.</li>);
  }
  if (items.some(item => typeof item.checkedOutBy === 'string')) {
    excuses.push(<li key="checkedOut">{documentWord} is checked out.</li>);
  }
  if (items.some(item => !item.pid.endsWith('-LATEST'))) {
    excuses.push(<li key="latest">{documentWord} is not the latest version.</li>);
  }
  if (items.some(item => item.envelopes.some(envelope => envelope.status === 'in progress'))) {
    excuses.push(<li key="signature">{documentWord} is in the signature process.</li>);
  }

  const signatureButton = excuses.length === 0
    ? <button type="button" onClick={props.signatureButtonClicked}>Sign document{items.length > 1 ? "s" : ""}</button>
    : <div><p style={{ fontWeight: 'bold' }}>Document{items.length > 1 ? "s" : ""} cannot be signed because:</p>
      <ul>
        {excuses}
      </ul>
    </div>;

  return <div>
    {items.length > 0 ? signatureButton : <p>Select a document first</p>}
  </div>
}

class SignatureFormTab extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      selected: null
    }

    this.handleSelectChange = this.handleSelectChange.bind(this);
  }

  handleSelectChange(e) {
    this.setState({ selected: e.target.value });
  }

  render() {
    const options = Array.isArray(this.props.templates)
      ? this.props.templates.map(template => <option key={template.name}>{template.name}</option>)
      : "";

    return <div>
      <select onChange={this.handleSelectChange}>
        <option value={null}>Select a template</option>
        {options}
      </select>
      <button type="button" onClick={() => this.props.sendEnvelope(this.state.selected)}>Send for signature</button>
      <br /><br />
      <button className="cancelButton" type="button" onClick={() => this.props.setTab(null)}>Cancel</button>
    </div>
  }
}

function EnvelopesTab(props) {
  const envelopes = props.item?.envelopes.map(
    envelope => <li key={envelope.signature}>
      <div>
          <p className="attribute"><span>Id:</span><span>{envelope.signature}</span></p>
          <p className="attribute"><span>Subject:</span><span>{envelope.subject}</span></p>
          <p className="attribute"><span>Status:</span><span>{envelope.status}</span></p>
          <div className="attribute non-flex"><span>Recipients:</span>{envelope.recipients.map(recipient => <p key={recipient.name} className="no-margin">{recipient.name} - {recipient.status}</p>)}</div>
          <div className="attribute non-flex"><span>Documents:</span>{envelope.documents.map(document => <p key={document.name} className="no-margin">{document.name}</p>)}</div>
      </div>
    </li>
  );

  return <div className="EnvelopesTab">
    <ul>
      {envelopes}
    </ul>
    <br/>
    <button className="cancelButton" type="button" onClick={() => props.setTab(null)}>Cancel</button>
  </div>
}

function getAttributeByName(item, attributeName) {
  const nameMatches = (attribute) => attribute.name === attributeName;
  return item.attributes.find(nameMatches);
}

function popupWindow(url, title, win, w, h) {
  let features = 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no';
  if (w > 0 && h > 0) {
    const y = win.outerHeight / 2 + win.screenY - (h / 2);
    const x = win.outerWidth / 2 + win.screenX - (w / 2);
    features += ', width=' + w + ', height=' + h + ', top=' + y + ', left=' + x;
  }

  return win.open(url, title, features);
}

export default App;
