import * as React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { StatusBar } from "expo-status-bar";
import { StyleSheet, Text, View } from "react-native";
import { ThemeProvider } from "./src/context/ThemeContext";
import { AuthNavigation } from "./src/pages/Auth/Routes";
import * as Font from "expo-font";
export const loadFonts = async () => {
  await Font.loadAsync({
    "OpenSans-Regular": require("./assets/fonts/OpenSans/Regular.ttf"),
    "OpenSans-Bold": require("./assets/fonts/OpenSans/Bold.ttf"),
    "OpenSans-SemiBold": require("./assets/fonts/OpenSans/SemiBold.ttf"),
    "OpenSans-Italic": require("./assets/fonts/OpenSans/Italic.ttf"),
    "OpenSans-ExtraBold": require("./assets/fonts/OpenSans/ExtraBold.ttf"),
    // Add more font variations as needed
  });
};
export default function App() {
  React.useEffect(() => {
    loadFonts();
  }, []);

  return (
    <ThemeProvider>
      <NavigationContainer>
        <AuthNavigation />
      </NavigationContainer>
    </ThemeProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
