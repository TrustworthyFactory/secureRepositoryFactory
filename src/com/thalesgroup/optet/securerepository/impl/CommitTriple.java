package com.thalesgroup.optet.securerepository.impl;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 * User: detonator
 * Date: 12.07.2009
 * Time: 21:41:37
 @author Vyacheslav Zholudev

 This class encapsulates the follwoign triple: file path in a repo,
 old input stream, new input stream
 */
public class CommitTriple {
    private String filePath;
    private InputStream baseData, workingData;
    private List<xSVNProperty> props;

    public CommitTriple(String filePath, InputStream baseData, InputStream workingData) {
        this.filePath = filePath;
        this.baseData = baseData;
        this.workingData = workingData;
        this.props = new ArrayList<xSVNProperty>(1);
    }

    public CommitTriple(String filePath, InputStream baseData, InputStream workingData, List<xSVNProperty> props) {
        this(filePath, baseData, workingData);
        if(props == null) {
            this.props = new ArrayList<xSVNProperty>(1);
        } else {
            this.props = props;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public InputStream getBaseData() {
        return baseData;
    }

    public InputStream getWorkingData() {
        return workingData;
    }

    public List<xSVNProperty> getProps() {
        return props;
    }

    public static List<String> getDocumentNames(List<CommitTriple> triples) {
        List<String> returnList = new ArrayList<String>(triples.size());
        for(CommitTriple triple: triples) {
            returnList.add(triple.getFilePath());
        }
        return returnList;
    }
}
