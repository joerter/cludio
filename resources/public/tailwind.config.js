/** @type {import('tailwindcss').Config} */
module.exports = {
  content: { relative: true, files: ["../../src/cludio/**/*.clj"] },
  theme: {
    extend: {},
  },
  plugins: [require("@tailwindcss/forms")],
};
