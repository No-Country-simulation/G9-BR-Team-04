import { CommonModule, NgTemplateOutlet } from '@angular/common';
import { Component, computed, input, signal, TemplateRef } from '@angular/core';

export interface TabItem {
  id: string;
  label: string;
  icon?: string;
  disabled?: boolean;
  content?: string;
  template?: TemplateRef<unknown> | null;
}

@Component({
  selector: 'app-tabs',
  standalone: true,
  imports: [CommonModule, NgTemplateOutlet],
  templateUrl: './tabs.html',
})
export class Tabs {
  tabs = input<TabItem[]>([]);
  activeTab = signal('');

  selectedTab = computed(() => {
    const items = this.tabs();
    if (!items.length) {
      return null;
    }

    const current = this.tabs().find((tab) => tab.id === this.activeTab());
    return current ?? items[0];
  });

  constructor() {
    this.activeTab.set(this.tabs()[0]?.id ?? '');
  }

  selectTab(tabId: string) {
    const tab = this.tabs().find((item) => item.id === tabId && !item.disabled);
    if (tab) {
      this.activeTab.set(tab.id);
    }
  }
}