/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        campus: {
          50: '#f3f4ff',
          100: '#e9e9ff',
          500: '#6558d3',
          600: '#5547bd',
          700: '#44399a'
        }
      }
    },
  },
  plugins: [],
}
