package com.bamoe.bpmnGererator.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class XmlUtil {

    private XmlUtil() {
    }

    public static Element create(Document document,
                                 Element parent,
                                 String tag) {

        Element element = document.createElement(tag);

        parent.appendChild(element);

        return element;
    }

    public static Element create(Document document,
                                 String tag) {

        return document.createElement(tag);
    }

    public static void attr(Element element,
                            String key,
                            String value) {

        if (value != null && !value.isBlank()) {
            element.setAttribute(key, value);
        }

    }

    public static void text(Document document,
                            Element parent,
                            String tag,
                            String value) {

        Element child = document.createElement(tag);

        child.setTextContent(value);

        parent.appendChild(child);

    }

}
