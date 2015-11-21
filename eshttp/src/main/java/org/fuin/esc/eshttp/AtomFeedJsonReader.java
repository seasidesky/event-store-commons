/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. <http://www.fuin.org/>
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
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.esc.eshttp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

import net.minidev.json.JSONArray;

import org.fuin.esc.api.CommonEvent;
import org.fuin.esc.spi.DeserializerRegistry;
import org.fuin.esc.spi.EnhancedMimeType;
import org.fuin.esc.spi.SerializedDataType;
import org.fuin.utils4j.Utils4J;

import com.jayway.jsonpath.JsonPath;

/**
 * Reads and JSON Atom feed.
 */
public final class AtomFeedJsonReader implements AtomFeedReader {

    @Override
    public final List<URI> readAtomFeed(final InputStream in) {

        final List<URI> uris = new ArrayList<>();

        final JsonReader jsonReader = Json.createReader(new InputStreamReader(in));
        final JsonObject jsonObj = jsonReader.readObject();
        final JSONArray read = JsonPath.read(jsonObj, "$.entries..id");
        if (read != null) {
            for (int i = 0; i < read.size(); i++) {
                final JsonString id = (JsonString) read.get(i);
                final String uri = id.getString();
                try {
                    uris.add(Utils4J.url(uri).toURI());
                } catch (final URISyntaxException ex) {
                    throw new RuntimeException("Couldn't create URI: " + uri);
                }
            }
        }

        return uris;

    }

    @Override
    public final CommonEvent readEvent(final DeserializerRegistry desRegistry,
            final SerializedDataType serMetaType, final InputStream in) {
        final AtomEntry<JsonObject> entry = readAtomEntry(in);

        return null;
    }

    /**
     * Parses the atom data without creating the event itself from data & meta data.
     * 
     * @param in
     *            Input stream to read.
     * 
     * @return Entry.
     */
    public final AtomEntry<JsonObject> readAtomEntry(final InputStream in) {

        final JsonReader jsonReader = Json.createReader(new InputStreamReader(in));
        final JsonObject jsonObj = jsonReader.readObject();

        final String eventStreamId = ((JsonString) JsonPath.read(jsonObj, "$.content.eventStreamId"))
                .getString();
        final int eventNumber = ((JsonNumber) JsonPath.read(jsonObj, "$.content.eventNumber")).intValue();
        final String eventType = ((JsonString) JsonPath.read(jsonObj, "$.content.eventType")).getString();
        final String eventId = ((JsonString) JsonPath.read(jsonObj, "$.content.eventId")).getString();
        final String dataContentTypeStr = ((JsonString) JsonPath.read(jsonObj,
                "$.content.metadata.EscSysMeta.data-content-type")).getString();
        final EnhancedMimeType dataContentType = EnhancedMimeType.create(dataContentTypeStr);
        final String metaContentTypeStr = ((JsonString) JsonPath.read(jsonObj,
                "$.content.metadata.EscSysMeta.meta-content-type")).getString();
        final EnhancedMimeType metaContentType = EnhancedMimeType.create(metaContentTypeStr);
        final JsonObject data = JsonPath.read(jsonObj, "$.content.data");
        final JsonObject meta = JsonPath.read(jsonObj, "$.content.metadata.EscUserMeta");

        return new AtomEntry<JsonObject>(eventStreamId, eventNumber, eventType, eventId, dataContentType,
                metaContentType, data, meta);

    }

}