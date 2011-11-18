package com.expressflow.services.amazons3;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.expressflow.security.KeyHolder;

public class AmazonS3Service {
	private AWSCredentials credentials;
	
	public String listBuckets(){
		return listBuckets(KeyHolder.AMAZON_ACCES_KEY, KeyHolder.AMAZON_SECRET_KEY);
	}
	
	public String listBuckets(String accessKey, String secret){
		String result = "";
		credentials = new BasicAWSCredentials(accessKey, secret);
		AmazonS3 s3 = new AmazonS3Client(credentials);
		Element xResult = new Element("buckets");
		for(Bucket bucket : s3.listBuckets()){
			Element xBucket = new Element("bucket");
			xBucket.setAttribute("name", bucket.getName());
			xBucket.setAttribute("creationDate", bucket.getCreationDate().toString());
			xBucket.setAttribute("owner", bucket.getOwner().getDisplayName());
			xResult.addContent(xBucket);
		}
		XMLOutputter serializer = new XMLOutputter();
		serializer.setFormat(Format.getPrettyFormat());
		result = serializer.outputString(xResult);
		result = StringEscapeUtils.unescapeXml(result);
		return result;
	}
}
