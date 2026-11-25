---
name: "Fridge Quest"
description: "A friendly, Material 3 mobile design system for students turning leftover fridge ingredients into quick, budget-friendly meals."
---

## Overview

Fridge Quest is built for college students staring into a half-empty fridge at 9 PM. The visual language blends **Material 3** clarity and tonal surface layering with playful, approachable warmth: generous rounded cards, crisp typographic hierarchy, and tactile pill chips. The mood is encouraging, resourceful, and fast—never preachy or fussy.

## Colors

The palette pairs a fresh **Mint Teal** primary (`#00685F`)—evoking clean kitchens and fresh ingredients—with a warm **Amber** secondary (`#904D00` / `#FE932C` container) for quest highlights, missing-ingredient callouts, and trophy moments. Surfaces use cool-minted neutrals (`#F5FAF8` base, `#FFFFFF` lowest container) so food photography and colorful ingredient chips pop without visual noise.

- **Primary (Mint Teal):** Main CTAs ("Next", "Start the quest"), active page dots, selected ingredient chips, and high-match recipe badges.
- **Secondary (Amber):** Quest accents, missing-ingredient highlights, timer callouts, and trophy/streak moments.
- **Surfaces:** Material 3 tonal hierarchy (`surface` → `surface-container-low` → `surface-container`) separates cards from the canvas without heavy borders.

```yaml
colors:
  light:
    primary: "#00685F"
    on-primary: "#FFFFFF"
    primary-container: "#008378"
    on-primary-container: "#F4FFFC"
    secondary: "#904D00"
    on-secondary: "#FFFFFF"
    secondary-container: "#FE932C"
    on-secondary-container: "#3C1C00"
    tertiary: "#924628"
    on-tertiary: "#FFFFFF"
    tertiary-container: "#B05E3D"
    on-tertiary-container: "#FFFBFF"
    error: "#BA1A1A"
    on-error: "#FFFFFF"
    error-container: "#FFDAD6"
    on-error-container: "#93000A"
    background: "#F5FAF8"
    on-background: "#171D1C"
    surface: "#F5FAF8"
    surface-dim: "#D6DBD9"
    surface-bright: "#F5FAF8"
    surface-container-lowest: "#FFFFFF"
    surface-container-low: "#F0F5F2"
    surface-container: "#EAEFED"
    surface-container-high: "#E4E9E7"
    surface-container-highest: "#DEE4E1"
    on-surface: "#171D1C"
    surface-variant: "#D8E5E2"
    on-surface-variant: "#3D4947"
    outline: "#6D7A77"
    outline-variant: "#BCC9C6"
    inverse-surface: "#2C3130"
    inverse-on-surface: "#EDF2F0"
    inverse-primary: "#6BD8CB"
    shadow: "#000000"
    scrim: "#000000"
  dark:
    primary: "#6BD8CB"
    on-primary: "#003732"
    primary-container: "#29A195"
    on-primary-container: "#00211D"
    secondary: "#FFB77D"
    on-secondary: "#4D2600"
    secondary-container: "#D97707"
    on-secondary-container: "#3C1C00"
    tertiary: "#FFB59A"
    on-tertiary: "#591C02"
    tertiary-container: "#D27956"
    on-tertiary-container: "#390E00"
    error: "#FFB4AB"
    on-error: "#690005"
    error-container: "#93000A"
    on-error-container: "#FFDAD6"
    background: "#0F1413"
    on-background: "#DEE4E1"
    surface: "#0F1413"
    surface-dim: "#0F1413"
    surface-bright: "#353A39"
    surface-container-lowest: "#0A0F0E"
    surface-container-low: "#171D1C"
    surface-container: "#1B2120"
    surface-container-high: "#262B2A"
    surface-container-highest: "#303635"
    on-surface: "#DEE4E1"
    surface-variant: "#3D4947"
    on-surface-variant: "#BCC9C6"
    outline: "#879391"
    outline-variant: "#3D4947"
    inverse-surface: "#DEE4E1"
    inverse-on-surface: "#2C3130"
    inverse-primary: "#006A61"
    shadow: "#000000"
    scrim: "#000000"
```

## Typography

**Alata** brings geometric warmth and friendly character to display titles, onboarding headlines, and recipe names. **Instrument Sans** handles all body copy, ingredient lists, match badges, and button labels with high legibility at small mobile sizes. **Source Code Pro** is reserved for numeric counters and prep-time readouts where tabular precision helps scanning.

```yaml
typography:
  display-lg:
    fontFamily: "Alata"
    fontSize: 36px
    fontWeight: 400
    lineHeight: 42px
    letterSpacing: -0.5px
  display-lg-mobile:
    fontFamily: "Alata"
    fontSize: 28px
    fontWeight: 400
    lineHeight: 34px
    letterSpacing: -0.25px
  headline-lg:
    fontFamily: "Alata"
    fontSize: 34px
    fontWeight: 400
    lineHeight: 40px
    letterSpacing: -0.25px
  headline-lg-mobile:
    fontFamily: "Alata"
    fontSize: 24px
    fontWeight: 400
    lineHeight: 30px
    letterSpacing: 0px
  headline-md:
    fontFamily: "Alata"
    fontSize: 20px
    fontWeight: 400
    lineHeight: 26px
    letterSpacing: 0px
  title-lg:
    fontFamily: "Instrument Sans"
    fontSize: 18px
    fontWeight: 600
    lineHeight: 24px
    letterSpacing: 0px
  title-md:
    fontFamily: "Instrument Sans"
    fontSize: 16px
    fontWeight: 600
    lineHeight: 22px
    letterSpacing: 0.1px
  body-lg:
    fontFamily: "Instrument Sans"
    fontSize: 16px
    fontWeight: 400
    lineHeight: 24px
    letterSpacing: 0.1px
  body-md:
    fontFamily: "Instrument Sans"
    fontSize: 14px
    fontWeight: 400
    lineHeight: 20px
    letterSpacing: 0.15px
  label-lg:
    fontFamily: "Instrument Sans"
    fontSize: 14px
    fontWeight: 600
    lineHeight: 20px
    letterSpacing: 0.1px
  label-md:
    fontFamily: "Instrument Sans"
    fontSize: 12px
    fontWeight: 600
    lineHeight: 16px
    letterSpacing: 0.2px
  label-sm:
    fontFamily: "Instrument Sans"
    fontSize: 11px
    fontWeight: 500
    lineHeight: 14px
    letterSpacing: 0.3px
  mono-sm:
    fontFamily: "Source Code Pro"
    fontSize: 12px
    fontWeight: 600
    lineHeight: 16px
    letterSpacing: 0px
```

## Layout & Spacing

Mobile-first **Fluid Grid** targeting `390px` viewports with `16px`–`20px` horizontal page margins and a strict `4px`/`8px` spacing scale. Onboarding screens use a single-column centered layout with generous breathing room between the illustration frame, copy block, and bottom navigation bar (`Skip`, page dots, `Next` / `Start the quest`). The recipe list screen stacks a compact top bar, horizontal scrollable ingredient chips, and a vertical feed of full-width rounded recipe cards.

```yaml
spacing:
  gutter-xs: 4px
  gutter-sm: 8px
  gutter-md: 12px
  gutter-lg: 16px
  gutter-xl: 24px
  section: 32px
  page-margin: 20px
```

## Elevation & Depth

Material 3 **tonal surface layering** provides primary hierarchy: `surface` (`#F5FAF8`) sits behind `surface-container-lowest` (`#FFFFFF`) recipe cards and `surface-container-low` (`#F0F5F2`) illustration stages. Elevated cards and primary buttons use soft, diffused ambient shadows (`0 2px 8px rgba(0, 32, 29, 0.06)`) rather than heavy drop shadows or dark borders. Subtle `1px` `outline-variant` borders appear only on unselected filter chips.

## Shapes

Friendly, expressive **Material 3 rounded geometry**: recipe cards use `xl` (`24px`) corners, media thumbnails inside cards step down to `lg` (`16px`), buttons and interactive chips use `full` (`9999px`) pill shapes, and badges use `sm` (`8px`) or `full`.

```yaml
rounded:
  sm: 8px
  DEFAULT: 12px
  md: 16px
  lg: 20px
  xl: 24px
  full: 9999px
```

## Motion & Micro-interaction

Motion feels snappy and buoyant. Chip toggles and button presses respond in `120ms`, while onboarding slide transitions and card reveals settle in `260ms` with Material 3 standard emphasized deceleration. Respect `prefers-reduced-motion` and animate only `transform`, `opacity`, and color.

```yaml
motion:
  state-change:
    duration: 120ms
    delay: 0ms
    timingFunction: ease-out
  surface-enter:
    duration: 260ms
    delay: 0ms
    timingFunction: cubic-bezier(0.2, 0, 0, 1)
  surface-exit:
    duration: 160ms
    delay: 0ms
    timingFunction: cubic-bezier(0.3, 0, 1, 1)
```

## Voice & Content Strategy

- **Tone of voice:** Encouraging, witty, practical—like a clever roommate who knows how to turn three random items into dinner. Never judgmental about messy fridges.
- **Capitalisation:** Sentence case for all headings, recipe titles, chips, and button labels.
- **Terminology:** Call the user's ingredients "your fridge" or "ingredients"; call saving recipes "collect trophies" or "cookbook".
- **Button labels:** Clear, concise action phrases (`Skip`, `Next`, `Start the quest`).
- **Numbers & units:** Prep time in minutes (`12 min`, `15 min`); ingredient match as `"You have 4 of 5"`.

## Components

Component implementations live as individual HTML sticker sheet files in
`components/`. Each file contains the canonical, styled HTML components with
`<!-- COMPONENT: name -->` comment markers. Read each file before use.

- **Badge** → `@components/badge.html` — `badge-numeric`, `badge-dot`
- **Button** → `@components/button.html` — `button-primary`, `button-icon`, `button-with-icon`
- **Card** → `@components/card.html` — `card-elevated`, `card-action`, `card-interactive`, `card-interactive-2`
- **Chip** → `@components/chip.html` — `chip-action`, `chip-input`, `chip-with-icon`
