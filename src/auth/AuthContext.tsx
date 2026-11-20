import { useQuery, useQueryClient } from '@tanstack/react-query';
import { createContext, useCallback, useContext, useMemo, type ReactNode } from 'react';
import { authApi } from '@/api/endpoints';
import type { Session } from '@/api/types';

type AuthState = {
  session: Session;
  loading: boolean;
  isLoggedIn: boolean;
  isAdmin: boolean;
  refresh: () => Promise<void>;
  logout: () => Promise<void>;
};

const AuthContext = createContext<AuthState | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const queryClient = useQueryClient();
  const { data, isLoading } = useQuery({
    queryKey: ['session'],
    queryFn: authApi.session,
    staleTime: 60_000,
    retry: false,
  });

  const refresh = useCallback(async () => {
    await queryClient.invalidateQueries({ queryKey: ['session'] });
  }, [queryClient]);

  const logout = useCallback(async () => {
    await authApi.logout().catch(() => undefined);
    queryClient.setQueryData(['session'], null);
    await queryClient.invalidateQueries();
  }, [queryClient]);

  const value = useMemo<AuthState>(() => {
    const session = data ?? null;
    return {
      session,
      loading: isLoading,
      isLoggedIn: !!session,
      isAdmin: !!session?.roles.includes('admin'),
      refresh,
      logout,
    };
  }, [data, isLoading, refresh, logout]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthState {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('AuthProvider 밖에서 useAuth 를 썼습니다.');
  return ctx;
}
