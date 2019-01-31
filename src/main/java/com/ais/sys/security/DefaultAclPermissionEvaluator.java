package com.ais.sys.security;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class DefaultAclPermissionEvaluator implements PermissionEvaluator {
	
	private final Log logger = LogFactory.getLog(getClass());

	private final AclService aclService;
	private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();
	private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();
	private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();
	private PermissionFactory permissionFactory = new DefaultPermissionFactory();

	public DefaultAclPermissionEvaluator(AclService aclService) {
		this.aclService = aclService;
	}

	/**
	 * Determines whether the user has the given permission(s) on the domain object using
	 * the ACL configuration. If the domain object is null, returns false (this can always
	 * be overridden using a null check in the expression itself).
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Object domainObject,
			Object permission) {
		if (domainObject == null) {
			return false;
		}
		
		
		Collection authorities = authentication.getAuthorities();
		Iterator<SimpleGrantedAuthority> it = authorities.iterator();
		while(it.hasNext()){
			String role = it.next().getAuthority();
			if("ROLE_SYSTEMADMIN".equals(role) || "ROLE_APPROVER".equals(role)){
				return true;
			}

		}

		ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy
				.getObjectIdentity(domainObject);

		ObjectIdentity oid = new ObjectIdentityImpl(objectIdentity.getType(),
				((BigDecimal)objectIdentity.getIdentifier()).longValue());
		return checkPermission(authentication, oid, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId,
			String targetType, Object permission) {
		ObjectIdentity objectIdentity = objectIdentityGenerator.createObjectIdentity(
				targetId, targetType);

		return checkPermission(authentication, objectIdentity, permission);
	}

	private boolean checkPermission(Authentication authentication, ObjectIdentity oid,
			Object permission) {
		// Obtain the SIDs applicable to the principal
		List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
		List<Permission> requiredPermission = resolvePermission(permission);

		final boolean debug = logger.isDebugEnabled();

		if (debug) {
			logger.debug("Checking permission '" + permission + "' for object '" + oid
					+ "'");
		}

		try {
			// Lookup only ACLs for SIDs we're interested in
			Acl acl = aclService.readAclById(oid, sids);

			if (acl.isGranted(requiredPermission, sids, false)) {
				if (debug) {
					logger.debug("Access is granted");
				}

				return true;
			}

			if (debug) {
				logger.debug("Returning false - ACLs returned, but insufficient permissions for this principal");
			}

		}
		catch (NotFoundException nfe) {
			if (debug) {
				logger.debug("Returning false - no ACLs apply for this principal",nfe);
			}
		}

		return false;

	}

	List<Permission> resolvePermission(Object permission) {
		if (permission instanceof Integer) {
			return Arrays.asList(permissionFactory.buildFromMask(((Integer) permission)
					.intValue()));
		}

		if (permission instanceof Permission) {
			return Arrays.asList((Permission) permission);
		}

		if (permission instanceof Permission[]) {
			return Arrays.asList((Permission[]) permission);
		}

		if (permission instanceof String) {
			String permString = (String) permission;
			Permission p;

			try {
				p = permissionFactory.buildFromName(permString);
			}
			catch (IllegalArgumentException notfound) {
				if (logger.isDebugEnabled()) {
					logger.debug("permission not found",notfound);
				}
				p = permissionFactory.buildFromName(permString.toUpperCase(Locale.ENGLISH));
			}

			if (p != null) {
				return Arrays.asList(p);
			}

		}
		throw new IllegalArgumentException("Unsupported permission: " + permission);
	}

	public void setObjectIdentityRetrievalStrategy(
			ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
		this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
	}

	public void setObjectIdentityGenerator(ObjectIdentityGenerator objectIdentityGenerator) {
		this.objectIdentityGenerator = objectIdentityGenerator;
	}

	public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
		this.sidRetrievalStrategy = sidRetrievalStrategy;
	}

	public void setPermissionFactory(PermissionFactory permissionFactory) {
		this.permissionFactory = permissionFactory;
	}
}
