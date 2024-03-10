import React, { useEffect } from "react";
import { themeStyles } from "../styles/theme";

type ThemeContextType = {
  theme: "light" | "dark";
  styles: Record<string, any>;
};
const themeContext = React.createContext<ThemeContextType>({
  theme: "light",
  styles: {},
});

export const ThemeProvider = ({
  children,
}: {
  children: React.ReactNode | React.ReactNode[];
}) => {
  const [theme, setTheme] = React.useState<"light" | "dark">("light");

  useEffect(() => {
    //TODO: get theme from local storage
  }, []);

  return (
    <themeContext.Provider
      value={{ theme: "light", styles: themeStyles[theme] }}
    >
      {children}
    </themeContext.Provider>
  );
};

export const useThemeContext = () => React.useContext(themeContext);
