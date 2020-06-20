package webhook.teamcity.server.rest.jersey;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jetbrains.annotations.NotNull;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import webhook.teamcity.settings.project.WebHookParameterStore;

@Provider
public class WebHookParameterStoreProvider implements InjectableProvider<Context, Type>, Injectable<WebHookParameterStore> {
	private final WebHookParameterStore parameterStore;

	public WebHookParameterStoreProvider(@NotNull final WebHookParameterStore webHookParameterStore) {
		this.parameterStore = webHookParameterStore;
	}

	public ComponentScope getScope() {
		return ComponentScope.Singleton;
	}

	public Injectable<WebHookParameterStore> getInjectable(final ComponentContext ic, final Context context, final Type type) {
		if (type.equals(WebHookParameterStore.class)) {
			return this;
		}
		return null;
	}

	public WebHookParameterStore getValue() {
		return parameterStore;
	}

}