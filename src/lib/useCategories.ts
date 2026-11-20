import { useQuery } from '@tanstack/react-query';
import { categoriesApi } from '@/api/endpoints';
import type { CategoryNode } from '@/api/types';

export function useCategories() {
  return useQuery({ queryKey: ['categories', 'tree'], queryFn: categoriesApi.tree, staleTime: 10 * 60_000 });
}

export function findCategory(tree: CategoryNode[] | undefined, name: string): CategoryNode | undefined {
  if (!tree) return undefined;
  for (const node of tree) {
    if (node.name === name) return node;
    const found = findCategory(node.children, name);
    if (found) return found;
  }
  return undefined;
}

/** 이름으로 찾은 카테고리의 조상 경로(루트 → 자신) */
export function categoryPath(tree: CategoryNode[] | undefined, name: string): CategoryNode[] {
  const walk = (nodes: CategoryNode[], trail: CategoryNode[]): CategoryNode[] | null => {
    for (const n of nodes) {
      const next = [...trail, n];
      if (n.name === name) return next;
      const found = walk(n.children, next);
      if (found) return found;
    }
    return null;
  };
  return (tree && walk(tree, [])) || [];
}

/** 이벤트 카테고리인지(이벤트 아래에 있으면 기간·상태 필터를 보여 준다) */
export function isEventCategory(tree: CategoryNode[] | undefined, name: string): boolean {
  return categoryPath(tree, name)[0]?.name === '이벤트';
}

export const categoryHref = (name: string) => `/posts?categoryName=${encodeURIComponent(name)}`;
