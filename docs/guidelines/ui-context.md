# UI Context: Aesthetics & UX Paradigms

## Aesthetic Tokens (Rose Pine Dark)
Ensure a unified UI generation by following these precise layout and color behaviors.

| Token | Hex | Usage |
|-------|-----|-------|
| `pine` | `#3b82f6` | Primary actions, health OK status |
| `love` | `#eb6f92` | Destructive actions, errors |
| `gold` | `#f6c177` | Warnings, checking states |
| `foam` | `#9ccfd8` | Secondary actions |
| `text` | `#e0def4` | Body text, labels |
| `surface` | `#1f1d2e` | Card backgrounds |

## Layout Behaviors
- **Spacing:** Use Tailwind's default scale (e.g., `gap-4`, `p-8`).
- **Radius:** `rounded-lg` (8px) for cards, `rounded-md` (4px) for buttons/inputs.
- **Center Focus:** Main dashboard content should be centered with a max-width of `max-w-4xl`.

## UX Paradigms

### 1. Anticipatory Banking
The UI should be proactive. Instead of just listing transactions, highlight trends or suggest actions (e.g., "You will exceed your budget").

### 2. Trust Architecture
- Use both color and icons for status indicators.
- Provide explicit feedback for every agentic action.
- Interactive elements must have a minimum touch target of 44x44px.

### 3. Accessibility (WCAG 2.2 AA)
- Keyboard-first interaction for all navigation.
- Mandatory `aria-label` for all icons and buttons.
- Visible `:focus` rings using `pine`.