package wrapper;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * Wraps the result provided by a Solr Query
 * @author 	Fernando Gomez
 * @date	3/29/2016
 */
public class SearchResult {
	private QueryResponse response;
	
	/**
	 * Constructor
	 * @author 	Fernando Gomez
	 * @date	3/29/2016
	 * @param resultCount
	 * @param start
	 */
	public SearchResult(QueryResponse response) {
		this.response = response;
	} 
	
	private SolrDocumentList getResult(){
		return response.getResults();
		
	}
	
	private SolrDocumentList getHighligtherResult(){
		Map<String,Map<String,List<String>>> highligth = response.getHighlighting();
		
		SolrDocumentList solrDocList = getResult();
		
		for (SolrDocument solrDoc: solrDocList){
			String id = (String)solrDoc.getFieldValue("id");
			Map<String,List<String>> solrDocHighlight = highligth.get(id);
			if (solrDocHighlight != null){
				List<String> title = solrDocHighlight.get("title");
				if (title != null && title.size() > 0){
					solrDoc.put("title_highlight", title.get(0));
				}
				List<String> sub_title = solrDocHighlight.get("sub_title");
				if (sub_title != null && sub_title.size() > 0){
					solrDoc.put("sub_title_highlight", sub_title.get(0));
				}
				List<String> body = solrDocHighlight.get("body");
				if (body != null && body.size() > 0){
					solrDoc.put("body_highlight", body.get(0));
				}
			}
		}
			
		return solrDocList;
	}
	
	@Override
	public String toString() {
		JSONObject obj, obt;
		JSONArray arr;
//		SolrDocumentList list = response.getResults();
		SolrDocumentList list = getHighligtherResult();
		
		obj = new JSONObject();
		obj.put("numFound", list.getNumFound());
		obj.put("start", list.getStart());
		
		// Modified by Michel 
		arr = new JSONArray();
		for (SolrDocument s : list) {
			obt = new JSONObject();
			obt.put("id", s.getFieldValue("id"));
			obt.put("title", s.getFieldValue("title"));
			obt.put("sub_title", s.getFieldValue("sub_title"));
			obt.put("fileID", s.getFieldValue("fileID"));
			obt.put("fileName", s.getFieldValue("fileName"));
			obt.put("body", s.getFieldValue("body"));
			//HighLight
			obt.put("title_highLight", s.getFieldValue("title_highlight"));
			obt.put("sub_title_highLight", s.getFieldValue("sub_title_highlight"));
			obt.put("body_highLight", s.getFieldValue("body_highlight"));
			arr.add(obt);
		}
		
		obj.put("docs", arr);
		return obj.toString();
	}
}
