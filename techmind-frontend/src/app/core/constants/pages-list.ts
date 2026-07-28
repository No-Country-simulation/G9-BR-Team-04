export interface PagesList {
    label: string
    path: string
    icon?: string
}

export const PAGES_LIST: PagesList[] = [
    { label: 'Início', path: '#', icon: 'home' },
    { label: 'Dashboard', path: '/', icon: 'chart-bar' },
]