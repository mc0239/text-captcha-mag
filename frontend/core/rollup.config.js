import resolve from "@rollup/plugin-node-resolve";
import commonjs from "@rollup/plugin-commonjs";
import alias from "@rollup/plugin-alias";
import replace from "@rollup/plugin-replace";
import babel from "@rollup/plugin-babel";
import styles from "rollup-plugin-styles";
import { terser } from "rollup-plugin-terser";
import pkg from "./package.json";

export default [
  {
    input: "src/main.js",
    output: {
      name: "TextCaptcha",
      file: pkg.browser,
      format: "umd",
      exports: "default",
    },
    plugins: [
      alias({
        entries: [
          { find: "react", replacement: "preact/compat" },
          { find: "react-dom", replacement: "preact/compat" },
        ],
      }),
      resolve({
        dedupe: ["react", "react-dom"],
        extensions: [".js", ".jsx"],
      }),
      commonjs(),
      replace({
        "process.env.NODE_ENV": JSON.stringify(process.env.NODE_ENV),
        preventAssignment: true,
      }),
      babel({
        babelHelpers: "runtime",
        babelrc: false,
        presets: ["@babel/preset-env", "@babel/preset-react"],
        plugins: ["@babel/plugin-transform-runtime"],
      }),
      styles({
        modules: true,
        autoModules: true,
      }),
      process.env.NODE_ENV === "production" && terser(),
    ],
  },
];
