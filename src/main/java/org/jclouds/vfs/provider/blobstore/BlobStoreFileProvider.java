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

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.http.HttpFileSystemConfigBuilder;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpUtils;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Adrian Cole
 */
public class BlobStoreFileProvider extends AbstractOriginatingFileProvider {

   public final static Collection<Capability> capabilities = Collections
            .unmodifiableCollection(Arrays.asList(Capability.CREATE, Capability.DELETE,
                     Capability.GET_TYPE, Capability.GET_LAST_MODIFIED, Capability.LIST_CHILDREN,
                     Capability.READ_CONTENT, Capability.URI, Capability.WRITE_CONTENT,
                     Capability.RANDOM_ACCESS_READ, Capability.ATTRIBUTES));

   public final static UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[] {
            UserAuthenticationData.USERNAME, UserAuthenticationData.PASSWORD };

   private final Iterable<Module> modules;

   public BlobStoreFileProvider() {
      this(ImmutableList.<Module> of(new Log4JLoggingModule()));
   }

   public BlobStoreFileProvider(Iterable<Module> modules) {
      this.modules = modules;
      setFileNameParser(new BlobStoreFileNameParser());
   }

   protected FileSystem doCreateFileSystem(final FileName name,
            final FileSystemOptions fileSystemOptions) throws FileSystemException {
      BlobStoreFileName rootName = (BlobStoreFileName) name;
      UserAuthenticationData authData = null;
      BlobStoreContext context;
      try {
         String uriToParse = rootName.getFriendlyURI();
         authData = UserAuthenticatorUtils.authenticate(fileSystemOptions, AUTHENTICATOR_TYPES);
         URI location = HttpUtils.createUri(uriToParse);
         Credentials credentials = new Credentials(UserAuthenticatorUtils.toString(UserAuthenticatorUtils.getData(
                                        authData, UserAuthenticationData.USERNAME, UserAuthenticatorUtils
                                        .toChar(rootName.getUserName()))), UserAuthenticatorUtils
                                        .toString(UserAuthenticatorUtils.getData(authData,
                                        UserAuthenticationData.PASSWORD, UserAuthenticatorUtils
                                        .toChar(rootName.getPassword()))));
         context = new BlobStoreContextFactory().createContext(
                    location.toString(),
                    credentials.identity,
                    credentials.credential,
                    modules );
      } finally {
         UserAuthenticatorUtils.cleanup(authData);
      }

      return new BlobStoreFileSystem(rootName, context, fileSystemOptions);
   }

   public FileSystemConfigBuilder getConfigBuilder() {
      return HttpFileSystemConfigBuilder.getInstance();
   }

   public Collection<Capability> getCapabilities() {
      return capabilities;
   }
}
