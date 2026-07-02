import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="empty-state">
      <span class="empty-icon">{{ icon }}</span>
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>
    </div>
  `,
  styles: [`
    .empty-state {
      text-align: center;
      padding: 48px 24px;
      color: #666;
    }
    .empty-icon {
      font-size: 48px;
      display: block;
      margin-bottom: 16px;
    }
    h3 {
      margin: 0 0 8px;
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }
    p {
      margin: 0;
      font-size: 14px;
    }
  `]
})
export class EmptyState {
  @Input() icon = '📭';
  @Input() title = 'No data found';
  @Input() message = 'Nothing to show here yet.';
}