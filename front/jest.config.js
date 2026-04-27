module.exports = {
  preset: "jest-preset-angular",
  globalSetup: "jest-preset-angular/global-setup",
  transform: {
    "^.+\\.(ts|js|html)$": "jest-preset-angular",
  },
  setupFiles: ["<rootDir>/jest.setup.js"],
};
