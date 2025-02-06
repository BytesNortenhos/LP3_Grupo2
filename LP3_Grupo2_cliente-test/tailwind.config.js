/** @type {import('tailwindcss').Config} */
module.exports = {
    mode: 'jit',
    content: [    
        "./src/pages/*.{html,js,ejs}",
        "./src/components/*.{html,js,ejs}",
    ],
    safelist: [
        'border-success',
        'border-error',
    ],
    theme: {
        extend: {
            fontFamily: {
                'default': ['Nunito'],
              },
        }
    },
    plugins: [
        require("@tailwindcss/typography"),
        require("daisyui")
    ],
    dasiyui: {
        themes: false,
        darkTheme: "dark",
        base: true,
        styled: true,
        utils: true,
        prefix: "",
        logs: true,
        themeRoot: ":root",
    },
};