# TennisTimeFrontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.9.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Using the app as a PWA on your phone

The frontend already ships with a manifest, icons, and a service worker that register only in production builds. Follow these steps to build, serve, and install it on a device:

1. Build the production bundle so the PWA assets are included:

   ```bash
   npx nx build frontend --configuration=production
   ```

2. Serve the built files from `dist/apps/frontend` over HTTPS or `http://localhost` (service workers are only allowed on secure origins). You can use the existing static server target:

   ```bash
   npx nx run frontend:serve-static
   ```

   By default this serves the production build on port 4200; open `http://localhost:4200/` in your browser.

3. On your phone, open the served URL and use the browser’s install option:
   - **Android (Chrome/Edge):** tap the menu (⋮) → **Add to Home screen** or **Install app**, then confirm.
   - **iOS/iPadOS (Safari):** tap the share sheet icon → **Add to Home Screen**, then tap **Add**.

After installation, launch the app from the home screen icon. It will run in standalone mode and use the service worker for offline caching and faster reloads. Reinstall or refresh the page to pick up updates when you publish a new build.