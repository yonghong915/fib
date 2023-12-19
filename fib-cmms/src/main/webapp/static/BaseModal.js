class BaseModal extends React.Component {
	state = {
		visible: false,
	}
	showModal = (e) => {
		console.log(`show ${this.props.hint} modal`)
		e.preventDefault()
		this.setState({ visible: true })
	}
	handleCancel = () => {
		console.log(`hide ${this.props.hint} modal`)
		this.setState({ visible: false })
	}
	handleOK = () => {
		console.log(`hide ${this.props.hint} modal`)
		this.setState({ visible: false })
	}
	renderAnchor = () => {
		if (this.props.type == 'button') {
			return <antd.Button type='primary' onClick={this.showModal}>{this.props.hint}</antd.Button>
		}
		return <span style={{ color: 'blue' }} onClick={this.showModal}>{this.props.hint}</span>
	}
	render() {
		console.log(`show ${this.props.hint} span`)
		const Component = this.props.form

		return (
			<span>
				{this.renderAnchor()}
				<Modal title={this.props.title} visible={this.state.visible}
					footer={null}
					onOk={this.handleOK}
					onCancel={this.handleCancel}>
					<Component onSubmit={this.handleOK} />
				</Modal>
			</span>
		)
	}
}