---
name: Kinetic HUD
colors:
  surface: '#0b1326'
  surface-dim: '#0b1326'
  surface-bright: '#31394d'
  surface-container-lowest: '#060e20'
  surface-container-low: '#131b2e'
  surface-container: '#171f33'
  surface-container-high: '#222a3d'
  surface-container-highest: '#2d3449'
  on-surface: '#dae2fd'
  on-surface-variant: '#c3c6d6'
  inverse-surface: '#dae2fd'
  inverse-on-surface: '#283044'
  outline: '#8d90a0'
  outline-variant: '#434654'
  surface-tint: '#b2c5ff'
  primary: '#b2c5ff'
  on-primary: '#002b73'
  primary-container: '#0052cc'
  on-primary-container: '#c4d2ff'
  inverse-primary: '#0c56d0'
  secondary: '#bac3ff'
  on-secondary: '#08218a'
  secondary-container: '#2c3ea3'
  on-secondary-container: '#a8b4ff'
  tertiary: '#00daf3'
  on-tertiary: '#00363d'
  tertiary-container: '#006470'
  on-tertiary-container: '#33e6ff'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#dae2ff'
  primary-fixed-dim: '#b2c5ff'
  on-primary-fixed: '#001848'
  on-primary-fixed-variant: '#0040a2'
  secondary-fixed: '#dee0ff'
  secondary-fixed-dim: '#bac3ff'
  on-secondary-fixed: '#00105c'
  on-secondary-fixed-variant: '#293ca0'
  tertiary-fixed: '#9cf0ff'
  tertiary-fixed-dim: '#00daf3'
  on-tertiary-fixed: '#001f24'
  on-tertiary-fixed-variant: '#004f58'
  background: '#0b1326'
  on-background: '#dae2fd'
  surface-variant: '#2d3449'
typography:
  display-lg:
    fontFamily: Hanken Grotesk
    fontSize: 56px
    fontWeight: '800'
    lineHeight: 64px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Hanken Grotesk
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Hanken Grotesk
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-caps:
    fontFamily: JetBrains Mono
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.1em
  timer-xl:
    fontFamily: JetBrains Mono
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.05em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 8px
  gutter: 16px
  margin-mobile: 20px
  margin-desktop: 32px
  container-max: 1200px
---

## Brand & Style

The design system is centered on high-efficiency task management through a "Heads-Up Display" (HUD) lens. The brand personality is clinical, precise, and authoritative, designed for users who treat their daily routines as a series of mission-critical objectives.

The aesthetic blends **Modern Corporate** reliability with a **Minimalist HUD** utility. It utilizes high-contrast interfaces and data-dense layouts to minimize cognitive load while maximizing information throughput. The "Ghost Mode" introduces elements of **Glassmorphism**, specifically for non-blocking progress indicators and background layers, ensuring that the primary focus remains on the active task timer or objective.

The emotional response should be one of "controlled urgency"—a feeling that the user has total command over their time and attention.

## Colors

This design system prioritizes a **Focus Mode (Dark)** as its primary state, utilizing a deep navy-slate (`#0F172A`) for surfaces to reduce eye strain and provide a high-contrast foundation for "Action Blue" elements.

- **Primary (Action Blue):** Used for primary calls to action, active task states, and critical progress indicators.
- **Secondary (Deep Indigo):** Used for subtle categorization, secondary buttons, and navigational depth.
- **Tertiary (Cyber Cyan):** Reserved exclusively for "Ghost Mode" highlights, timers, and data-visualization accents.
- **Neutral:** A range of high-contrast grays and slates to maintain a clean hierarchy.

In Light Mode, the primary blue remains, but the background shifts to a clinical white (`#F8FAFC`) with borders replacing shadows for structural definition.

## Typography

The typography strategy leverages three distinct typefaces to reinforce the HUD aesthetic:

1.  **Hanken Grotesk (Headlines):** Used for high-impact task titles and screen headers. Its sharp, contemporary geometry provides a professional, "SaaS-native" feel.
2.  **Inter (Body):** Used for descriptions, notes, and general UI labels. Chosen for its exceptional legibility at small sizes and neutral tone.
3.  **JetBrains Mono (Data/Labels):** Used for timers, progress percentages, and metadata labels. The monospaced nature ensures that ticking timers do not cause layout shifts and reinforces the technical, utility-focused narrative.

All labels should be rendered in uppercase with increased letter-spacing to mimic instrument panel readouts.

## Layout & Spacing

The system follows a strict **8px grid** to ensure a disciplined, technical layout. 

- **Grid Model:** A 12-column fluid grid for desktop and a 4-column grid for mobile. 
- **HUD Density:** Elements are packed with "Functional Density." Instead of wide-open whitespace, the system uses clear structural dividers and "data blocks" to group related information.
- **Ghost Mode Layout:** In Focus/Ghost mode, the center of the screen is reserved for a single primary metric (e.g., a countdown), while peripheral tasks are relegated to translucent "Ghost Bars" at the bottom of the viewport.
- **Safe Zones:** 20px horizontal margins on mobile ensure that high-contrast text doesn't bleed into the physical edges of the device display.

## Elevation & Depth

This design system avoids traditional soft shadows in favor of **Tonal Layering** and **Ghosting**.

- **Surface Tiers:** Background uses the darkest neutral. Containers use a slightly lighter "Surface" color. "Active" containers use an even lighter tint or a subtle 1px border in the primary color.
- **Ghost Mode Depth:** Utilizes `backdrop-filter: blur(12px)` and `opacity: 0.1` for background elements. This creates a "glass" effect where secondary tasks appear to be behind the primary focus area, visually indicating they are inactive but present.
- **Outlines:** Instead of shadows, use 1px or 2px solid strokes (`#FFFFFF10` in dark mode) to define card boundaries. This maintains a flat, technical HUD look.

## Shapes

The shape language is **Soft (0.25rem)**. 

While Material 3 often uses very rounded corners, this design system limits corner radii to maintain a "precise instrument" feel. Buttons and input fields use a 4px (0.25rem) radius. Large containers or cards may use an 8px (0.5rem) radius. 

**Progress Bars:** Progress indicators are the exception—they should be perfectly rectangular (0px radius) to emphasize a linear, technical "loading" or "completion" state.

## Components

- **Buttons:** Primary buttons are solid "Action Blue" with white JetBrains Mono text. Secondary buttons are outlined with no fill.
- **Ghost Progress Bars:** Use a secondary color background at 10% opacity, with a high-contrast tertiary cyan foreground. In Focus Mode, these bars should be thin (4px height) to minimize visual noise.
- **HUD Cards:** No shadows. Use 1px borders. Header area of the card should be a slightly darker tonal shade than the card body.
- **Timers:** Large, center-aligned JetBrains Mono text. Use "Cyber Cyan" for the active state and "Deep Indigo" for paused states.
- **Inputs:** Underlined or "Infilled" style with 0.25rem top-rounded corners. Use a high-contrast blinking block cursor (monospaced style).
- **Task Chips:** Rectangular with 4px radius. Use labels in `label-caps` typography style. Use color-coding strictly for priority (Red: High, Blue: Normal, Gray: Routine).
- **Control Toggle:** Use Material 3 Switch logic, but with a squared-off thumb and high-contrast color transitions.