/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.vfs.provider.blobstore;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.URLFileNameParser;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.provider.VfsComponentContext;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpUtils;

import java.net.URI;

/**
 * @author Adrian Cole
 */
public class BlobStoreFileNameParser extends URLFileNameParser {
   public static final BlobStoreFileNameParser INSTANCE = new BlobStoreFileNameParser();

   public static BlobStoreFileNameParser getInstance() {
      return INSTANCE;
   }

   public BlobStoreFileNameParser() {
      super(443);
   }

   public FileName parseUri(final VfsComponentContext context, FileName base, String filename)
            throws FileSystemException {

      // if there are unencoded characters in the password, things break.
      URI uri = HttpUtils.createUri(filename);

      filename = uri.toASCIIString();

      Credentials creds = Credentials.parse(uri);

      StringBuilder name = new StringBuilder();

      // Extract the scheme and authority parts
      Authority auth = extractToPath(filename, name);

      // Decode and adjust separators
      UriParser.canonicalizePath(name, 0, name.length(), this);
      UriParser.fixSeparators(name);

      // Extract the container
      String container = UriParser.extractFirstElement(name);
      if (container == null || container.length() == 0) {
         throw new FileSystemException("vfs.provider.blobstore/missing-container-name.error",
                  filename);
      }

      // Normalise the path. Do this after extracting the container name
      FileType fileType = UriParser.normalisePath(name);
      String path = name.toString();

      return new BlobStoreFileName(auth.getHostName(), creds.credential, creds.identity, path, fileType,
               container);
   }

}
