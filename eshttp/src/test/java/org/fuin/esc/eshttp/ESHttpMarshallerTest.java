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
package org.fuin.esc.eshttp;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.api.EventId;
import org.fuin.esc.api.SimpleCommonEvent;
import org.fuin.esc.spi.EscEvent;
import org.fuin.esc.spi.EscEvents;
import org.fuin.esc.spi.EscMeta;
import org.fuin.esc.spi.JsonDeSerializer;
import org.fuin.esc.spi.SerDeserializerRegistry;
import org.fuin.esc.spi.SerializedDataType;
import org.fuin.esc.spi.SimpleSerializerDeserializerRegistry;
import org.fuin.esc.spi.XmlDeSerializer;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

/**
 * Tests the {@link ESHttpMarshaller} class.
 */
// CHECKSTYLE:OFF Test
public class ESHttpMarshallerTest {

    @Test
    public void testMarshalEventXML() throws IOException {

        // PREPARE
        final String expectedXml = IOUtils.toString(
                this.getClass().getResourceAsStream("/" + this.getClass().getSimpleName() + "_Event.xml"));
        final UUID uuid = UUID.fromString("a07d6a1a-715d-4d8e-98fa-b158e3339303");
        final CommonEvent commonEvent = new SimpleCommonEvent(new EventId(uuid), MyEvent.TYPE,
                new MyEvent(uuid, "Whatever..."));
        final ESHttpMarshaller testee = new ESHttpMarshaller();

        // TEST
        final String currentXml = testee.marshal(createXmlRegistry(), commonEvent);

        // VERIFY
        final Diff documentDiff = DiffBuilder.compare(expectedXml).withTest(currentXml).ignoreWhitespace()
                .build();
        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

    }

    @Test
    public void testMarshalEventJSON() throws IOException {

        // PREPARE
        final String expectedJson = IOUtils.toString(
                this.getClass().getResourceAsStream("/" + this.getClass().getSimpleName() + "_Event.json"));

        final UUID uuid = UUID.fromString("a07d6a1a-715d-4d8e-98fa-b158e3339303");
        final JsonObject myEvent = Json.createObjectBuilder().add("id", uuid.toString())
                .add("description", "Whatever...").build();
        final CommonEvent commonEvent = new SimpleCommonEvent(new EventId(uuid), MyEvent.TYPE, myEvent);
        final ESHttpMarshaller testee = new ESHttpMarshaller();

        // TEST
        final String currentJson = testee.marshal(createJsonRegistry(), commonEvent);

        // TEST
        assertThatJson(currentJson).isEqualTo(expectedJson);

    }

    @Test
    public void testMarshalEventsXML() throws IOException {

        // PREPARE
        final String expectedXml = IOUtils.toString(
                this.getClass().getResourceAsStream("/" + this.getClass().getSimpleName() + "_Events.xml"));

        final UUID uuid1 = UUID.fromString("a07d6a1a-715d-4d8e-98fa-b158e3339303");
        final CommonEvent commonEvent1 = new SimpleCommonEvent(new EventId(uuid1), MyEvent.TYPE,
                new MyEvent(uuid1, "Whatever..."));

        final UUID uuid2 = UUID.fromString("ee9e13fc-e799-4835-9830-ecb9d620c1c9");
        final CommonEvent commonEvent2 = new SimpleCommonEvent(new EventId(uuid2), MyEvent.TYPE,
                new MyEvent(uuid2, "Hello XML"));

        final List<CommonEvent> commonEvents = new ArrayList<>();
        commonEvents.add(commonEvent1);
        commonEvents.add(commonEvent2);

        final ESHttpMarshaller testee = new ESHttpMarshaller();

        // TEST
        final String currentXml = testee.marshal(createXmlRegistry(), commonEvents);

        // VERIFY
        final Diff documentDiff = DiffBuilder.compare(expectedXml).withTest(currentXml).ignoreWhitespace()
                .build();
        assertThat(documentDiff.hasDifferences()).describedAs(documentDiff.toString()).isFalse();

    }

    @Test
    public void testMarshalEventsJSON() throws IOException {

        // PREPARE
        final String expectedJson = IOUtils.toString(
                this.getClass().getResourceAsStream("/" + this.getClass().getSimpleName() + "_Events.json"));

        final UUID uuid1 = UUID.fromString("a07d6a1a-715d-4d8e-98fa-b158e3339303");
        final JsonObject myEvent1 = Json.createObjectBuilder().add("id", uuid1.toString())
                .add("description", "Whatever...").build();
        final CommonEvent commonEvent1 = new SimpleCommonEvent(new EventId(uuid1), MyEvent.TYPE, myEvent1);

        final UUID uuid2 = UUID.fromString("ee9e13fc-e799-4835-9830-ecb9d620c1c9");
        final JsonObject myEvent2 = Json.createObjectBuilder().add("id", uuid2.toString())
                .add("description", "Hello JSON").build();
        final CommonEvent commonEvent2 = new SimpleCommonEvent(new EventId(uuid2), MyEvent.TYPE, myEvent2);

        final List<CommonEvent> commonEvents = new ArrayList<>();
        commonEvents.add(commonEvent1);
        commonEvents.add(commonEvent2);

        final ESHttpMarshaller testee = new ESHttpMarshaller();

        // TEST
        final String currentJson = testee.marshal(createJsonRegistry(), commonEvents);

        // TEST
        assertThatJson(currentJson).isEqualTo(expectedJson);

    }

    private SerDeserializerRegistry createXmlRegistry() {
        final XmlDeSerializer xmlDeSer = new XmlDeSerializer(false, MyEvent.class, EscEvent.class,
                EscEvents.class, EscMeta.class);
        final SimpleSerializerDeserializerRegistry registry = new SimpleSerializerDeserializerRegistry();
        registry.add(new SerializedDataType(MyEvent.TYPE.asBaseType()), "application/xml", xmlDeSer);
        registry.add(new SerializedDataType(EscEvents.TYPE.asBaseType()), "application/xml", xmlDeSer);
        registry.add(new SerializedDataType(EscEvent.TYPE.asBaseType()), "application/xml", xmlDeSer);
        registry.add(new SerializedDataType(EscMeta.TYPE.asBaseType()), "application/xml", xmlDeSer);
        return registry;
    }

    private SerDeserializerRegistry createJsonRegistry() {
        final SimpleSerializerDeserializerRegistry registry = new SimpleSerializerDeserializerRegistry();
        final JsonDeSerializer jsonDeSer = new JsonDeSerializer();
        registry.add(new SerializedDataType(MyEvent.TYPE.asBaseType()), "application/json", jsonDeSer);
        registry.add(new SerializedDataType(EscEvents.TYPE.asBaseType()), "application/json", jsonDeSer);
        registry.add(new SerializedDataType(EscEvent.TYPE.asBaseType()), "application/json", jsonDeSer);
        registry.add(new SerializedDataType(EscMeta.TYPE.asBaseType()), "application/json", jsonDeSer);
        return registry;
    }

}
// CHECKSTYLE:ON
