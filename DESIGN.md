# Design System — Fintech FIAP 2026

## Philosophy

Clean, accessible, and trustworthy. The visual language mirrors the project's thesis on "Autonomy and Empathy" — sharp data presentation softened by human-centric interaction patterns.

## Theme

**Rose Pine** — adapted for dark mode by default. Colors communicate state clearly without being alarming.

### Color Palette

| Token | Hex | Usage |
|-------|-----|-------|
| `pine` | `#3b82f6` | Primary actions, health OK status, links |
| `love` | `#eb6f92` | Destructive actions, logout, errors |
| `gold` | `#f6c177` | Warnings, checking states, highlights |
| `foam` | `#9ccfd8` | Secondary actions, sign-in button |
| `text` | `#e0def4` | Body text, labels |
| `subtle` | `#908caa` | Muted captions, secondary info |
| `muted` | `#6e6a86` | Placeholder text, borders |
| `surface` | `#1f1d2e` | Card backgrounds, inputs |
| `highlight-med`| `#26233a` | Borders, dividers |

### Status Colors

- **Healthy / OK**: `bg-pine/20 text-pine`
- **Checking**: `bg-gold/20 text-gold`
- **Error / Unreachable**: `bg-love/20 text-love`

## Typography

- **Font family**: System sans-serif (default browser stack)
- **Heading scale**:
  - H1: `text-4xl font-bold` — page titles
  - Body: `text-sm` / `text-base` — readable at all breakpoints
- **Monospace**: Used for API status badges, log output

## Spacing & Layout

- Main content is centered flex column: `flex flex-col items-center justify-center p-8`
- Card/form width: fixed ~288px (`w-72`) for auth forms
- Padding: `p-8` outer, `gap-3` / `gap-2` inner
- Border radius: `rounded` (4px default) for buttons and inputs

## Components

### Input Fields

```
bg-surface
text-text
border border-highlight-med
rounded
px-3 py-2
placeholder:text-muted
```

### Buttons

- **Primary (Sign In)**: `bg-foam text-base px-4 py-2 rounded hover:opacity-80`
- **Destructive (Logout)**: `bg-love text-base px-4 py-2 rounded hover:opacity-80`

### Status Badge

```
font-mono text-sm
px-2 py-0.5 rounded
```

With conditional background and text color based on status value.

## Accessibility (WCAG 2.2 AA)

- All color contrast ratios meet AA standards
- Interactive elements have visible `:focus` states (Tailwind default ring)
- Form inputs have associated labels or `aria-label`
- Status indicators use both color AND text (non-color dependent)
- Touch targets are minimum 44×44px

## Responsive

The app is currently optimized for desktop/tablet viewports. Mobile responsiveness is a Phase 1 completion goal before phase transition.

## Assets

- `assets/Projeto Fintech 2026.png` — Dashboard concept reference
- `assets/The Synthesis of Autonomy and Empathy...pdf` — Strategic design analysis for 2026

## Future Work

- Component library extraction (Phase 4)
- Figma token sync (currently manual)
- Motion design (micro-interactions on state transitions)
