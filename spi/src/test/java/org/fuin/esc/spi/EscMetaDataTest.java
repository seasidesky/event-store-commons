/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.esc.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.JaxbUtils.unmarshal;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

/**
 * Test for {@link EscMetaData} class.
 */
public class EscMetaDataTest {

    private static final String CONTENT_TYPE = "application/xml; version=1; encoding=utf-8";

    @Test
    public final void testUnMarshal() throws Exception {

        // PREPARE
        final String expectedXml = IOUtils
                .toString(this.getClass().getResourceAsStream("/meta-data-xml.xml"));

        // TEST
        final EscMetaData testee = unmarshal(expectedXml, EscMetaData.class, MyMeta.class, Base64Data.class);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getEscMeta()).isNotNull();
        final EscMeta meta = testee.getEscMeta();
        assertThat(meta.getUserMeta()).isNotNull();
        assertThat(meta.getUserMeta().getObj()).isInstanceOf(MyMeta.class);
        final MyMeta userMeta = (MyMeta) meta.getUserMeta().getObj();
        assertThat(userMeta.getUser()).isEqualTo("abc");

        assertThat(meta.getSysMeta()).isNotNull();
        assertThat(meta.getSysMeta().getDataContentType().toString()).isEqualTo(CONTENT_TYPE);
        assertThat(meta.getSysMeta().getMetaContentType().toString()).isEqualTo(CONTENT_TYPE);
        assertThat(meta.getSysMeta().getMetaType().toString()).isEqualTo("MyMeta");

        // TEST
        final String xml = marshal(testee, EscMetaData.class, MyMeta.class, Base64Data.class);

        // VERIFY
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(expectedXml, xml);

    }

    @Test
    public final void testToJson() {

        // PREPARE
        final EnhancedMimeType dataContentType = EnhancedMimeType.create("application/xml");
        final EnhancedMimeType metaContentType = EnhancedMimeType
                .create("text/plain; transfer-encoding=base64");
        final String metaType = "JustText";
        final String base64Str = "SGVsbG8gd29ybGQh";
        final EscSysMeta sysMeta = new EscSysMeta(dataContentType, metaContentType, metaType);
        final DataWrapper userMeta = new DataWrapper(new Base64Data(base64Str));
        final EscMeta meta = new EscMeta(sysMeta, userMeta);
        final EscMetaData testee = new EscMetaData(meta);

        // TEST
        final JsonObject result = testee.toJson();

        // VERIFY
        assertThat(result.getJsonObject("EscMeta")).isNotNull();
        final JsonObject jsonMeta = result.getJsonObject("EscMeta");
        assertThat(jsonMeta.getJsonObject("EscSysMeta")).isNotNull();
        assertThat(jsonMeta.getJsonObject("EscUserMeta")).isNotNull();
        assertThat(jsonMeta.getJsonObject("EscSysMeta").getString("data-content-type")).isEqualTo(
                dataContentType.toString());
        assertThat(jsonMeta.getJsonObject("EscSysMeta").getString("meta-content-type")).isEqualTo(
                metaContentType.toString());
        assertThat(jsonMeta.getJsonObject("EscSysMeta").getString("meta-type")).isEqualTo(metaType);
        assertThat(jsonMeta.getJsonObject("EscUserMeta").getString("Base64")).isEqualTo(base64Str);

    }

}
