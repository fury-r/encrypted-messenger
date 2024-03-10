import { StyleSheet } from "react-native";
import { Theme } from "../types/theme";

export const themeStyles: Theme = StyleSheet.create({
  dark: {
    color: "white",
    backgroundColor: "#696969",
  },
  light: {
    color: "black",
    backgroundColor: "#FC6B68",
  },
});
