import React, { useCallback, useMemo } from "react";
import { useThemeContext } from "../../../../context/ThemeContext";
import { onChange } from "../../../../utils/Onchange";

type InputState = {
  email: string;
  password: string;
  remember?: boolean;
};

interface ILogin {
  inputState: InputState;
  handleOnChange: (e: any, key: string) => void;
  handleSubmit: () => void;
}
export const useLogin = () => {
  const { theme } = useThemeContext();
  const [inputState, setInputState] = React.useState<InputState>({
    email: "",
    password: "",
    remember: false,
  });

  const [error, setError] = React.useState<InputState>({
    email: "",
    password: "",
  });
  const handleOnChange = useCallback(
    (e: any, key: string) => onChange(e, setInputState, key),
    [onChange, setInputState]
  );

  const handleSubmit = useCallback(() => {}, [
    inputState,
    setInputState,
    error,
    setError,
  ]);

  const state: ILogin = useMemo(
    () => ({ inputState, handleOnChange, handleSubmit }),
    [inputState, handleOnChange, handleSubmit]
  );

  return state;
};
