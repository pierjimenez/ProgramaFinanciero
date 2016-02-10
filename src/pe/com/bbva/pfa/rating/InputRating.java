package pe.com.bbva.pfa.rating;

import java.util.List;

public class InputRating {
	
	private HeaderRating header;
	private List<DataRating> data;
	
	public HeaderRating getHeader() {
		return header;
	}
	public void setHeader(HeaderRating header) {
		this.header = header;
	}
	public List<DataRating> getData() {
		return data;
	}
	public void setData(List<DataRating> data) {
		this.data = data;
	}

	
	
}
