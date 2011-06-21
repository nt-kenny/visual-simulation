package diagram;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.apache.crimson.tree.XmlDocument;

public class ConnectorExporterControl extends ExporterControl {


	public Element exportElement(DiagramElement _diagramElement)
	{
		ConnectorExporter _connectorExporter = new ConnectorExporter(_diagramElement, _document);
		Element _diagramElementTag = _connectorExporter.createDiagramElementTag();
		Vector _pointElementTags = _connectorExporter.createPointElementTags();
		Vector _reshapePointElementTags = _connectorExporter.createReshapePointElementTags();

		for(int i= 0;i<_pointElementTags.size(); i++) {
			_diagramElementTag.appendChild((Element)_pointElementTags.elementAt(i));
		}

		for(int i= 0;i<_reshapePointElementTags.size(); i++) {
			_diagramElementTag.appendChild((Element)_reshapePointElementTags.elementAt(i));
		}
		return _diagramElementTag;
	}

	public Element exportAssociation(Association _association){return null;}
}