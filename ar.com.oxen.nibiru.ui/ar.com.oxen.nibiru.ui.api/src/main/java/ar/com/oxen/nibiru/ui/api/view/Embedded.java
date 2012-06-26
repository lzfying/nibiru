package ar.com.oxen.nibiru.ui.api.view;


public interface Embedded extends Component {
	void setData(byte[] data);

	void setMimeType(String mimeType);

	String getMimeType();
}
