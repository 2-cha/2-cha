"use client";

import {
  Button,
  FormControl,
  FormErrorMessage,
  FormLabel,
  Input,
  VStack,
} from "@chakra-ui/react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";

interface LoginFormData {
  email: string;
  password: string;
}

export default function Login() {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    setError,
  } = useForm<LoginFormData>();

  const onSubmit = handleSubmit(async (data) => {
    try {
      const res = await fetch("/login/api", {
        method: "POST",
        body: JSON.stringify(data),
      });
      if (!res.ok) {
        throw new Error();
      }

      router.push("/");
    } catch {
      setError("root", { message: "Invalid email or password" });
    }
  });

  return (
    <form onSubmit={onSubmit}>
      <VStack gap={4} align="start">
        <FormControl isInvalid={errors.email != null}>
          <FormLabel htmlFor="email">Email</FormLabel>
          <Input
            type="text"
            id="email"
            {...register("email", { required: true })}
          />
          <FormErrorMessage>
            {errors.email && errors.email.message}
          </FormErrorMessage>
        </FormControl>

        <FormControl isInvalid={errors.password != null}>
          <FormLabel htmlFor="password">Password</FormLabel>
          <Input
            type="password"
            id="password"
            {...register("password", { required: true })}
          />
          <FormErrorMessage>
            {errors.password && errors.password.message}
          </FormErrorMessage>
        </FormControl>

        <Button type="submit" isLoading={isSubmitting} colorScheme="teal">
          Login
        </Button>
      </VStack>
    </form>
  );
}
